/**
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * <p>
 * {description}
 * Copyright (C) 2014 Pascal Poizat (@pascalpoizat)
 * emails: pascal.poizat@lip6.fr
 */

package transformations.bpmn2cif;

import java.util.HashMap;
import java.util.List;

import models.base.IllegalResourceException;
import models.choreography.bpmn.BpmnFactory;
import models.choreography.cif.CifFactory;
import models.choreography.cif.CifModel;
import models.choreography.cif.generated.*;
import org.eclipse.bpmn2.*;
import models.base.IllegalModelException;
import models.choreography.cif.generated.Message;
import transformations.base.AbstractTransformer;
import models.choreography.bpmn.BpmnModel;

/**
 * Created by Pascal Poizat (@pascalpoizat) on 10/01/2014.
 */
public class Bpmn2CifTransformer extends AbstractTransformer {

    // CONSTRAINTS
    //
    // IDS VS. NAMES
    //
    // BPMN model supports id and name for elements
    // CIF model only supports id
    // Either BPMN id or BPMN name can be used as CIF id.
    // The one that is used must fulfil these constraints:
    //	- exist in the BPMN model
    //	- be unique in its category
    //	- be correct wrt CIF naming constraints
    // note : ids should be used in formal verification
    //        and names for user experience (modeling, verification results)
    //
    // Choreography			: name (id if undef)				[UNCHECKED]
    // Participant			: name (id if undef)				[UNCHECKED]
    // StartEvent			: id								[UNCHECKED]
    // EndEvent				: id								[UNCHECKED]
    // ChoreographyTask		: id								[UNCHECKED]
    // Message				: Choreography Task name (id)		[UNCHECKED]
    //
    // PREDECESSOR / SUCCESSOR
    //
    // StartEvent			: 0 / 1								[CHECKED (warning)]
    // EndEvent				: 1 / 0								[CHECKED (warning)]
    // ChoreographyTask		: 1 / 1								[CHECKED (warning)]
    // Gateway (conv.)		: n / 1								[CHECKED (warning, error)]
    // Gateway (div.)		: 1 / n								[CHECKED (warning, error)]
    //
    // CONSTRAINTS (SOME DUE TO CIF MODEL)
    //
    // Constraints on id vs. names above for all elements 		[UNCHECKED]
    // Constraints on predecessor / successor for flow nodes	[UNCHECKED]
    // BPMN model should have exactly one start event			[CHECKED (warning)]
    // BPMN model should have exactly one end event				[CHECKED (warning)]
    // BPMN model should use Start/End events instead of
    //	setting other kinds of flow nodes to be initial/final	[UNCHECKED]
    // Gateways should be either converging or diverging		[CHECKED (error)]
    // Participants in Choreo. and Choreo.Tasks consistent		[UNCHECKED]
    // Messages in Choreo. and Choreo.Tasks consistent			[UNCHECKED]
    // Choreo. Tasks have 1 sender and 1 receiver (can be =)	[CHECKED (warning)]
    //
    // LIMITS
    //
    // Message ids are generated from ChoreographyTask name (or ids)
    // Loop types not supported in CIF ?
    // Some gateways are not supported (see below, TRANSFORMATION)
    //
    // ORGANISATION
    //
    // The BPMN file should be organized as for choreographies created with the ECLIPSE BPMN2 CHOREOGRAPHY WIZARD
    // (organization of BPMN file in general is unclear)
    //
    // <bpmn2:definitions>
    //	<bpmn2:choreography ...>
    //		... // mixed list including participant defs, sequence flows, flow elements, etc.
    //	</bpmn2:choreography>
    //	<bpmndi:BPMNDiagram ...>
    //		... // diagram information for the BPMN choreography (placement, etc.)
    //	</bpmndi:BPMNDiagram>
    // </bpmn2:definitions>
    //
    // TRANSFORMATION
    //
    // diverging ParallelGateway -> AllSelectState
    // diverging ExclusiveGateway -> ChoiceState
    // diverging InclusiveGateway -> SubsetSelectState
    // diverging ComplexGateway -> ??
    // diverging EventBasedGateWay -> ??
    // ?? -> DominatedChoiceState
    //
    // converging ParallelGateway -> AllJoinState
    // converging ExclusiveGateway -> SimpleJoinState
    // converging InclusiveGateway -> SubsetJoinState
    // converging ComplexGateway -> ??
    // converging EventBasedGateWay -> ??
    //

    public static final String NAME = "bpmn2cif";
    public static final String VERSION = "1.1";

    // CHANGELOG
    // version 1.1 changes:
    // - if messages are associated to choreography tasks, they are used
    //   else the behaviour is as in v1.0 (the choreography task information is used)

    HashMap<String, String> participants;   // participant id in bpmn model -> participant id in cif model
    HashMap<String, String> messages;       // choreography task id in bpmn model -> message id in cif model

    boolean has_initial_state;
    boolean has_final_state;

    public Bpmn2CifTransformer() {
        super();
        participants = new HashMap<String, String>();
        messages = new HashMap<String, String>();
        has_initial_state = false;
        has_final_state = false;
    }

    @Override
    public void transform() throws IllegalResourceException, IllegalModelException {
        checkModel(inputModel, BpmnModel.class);
        checkModel(outputModel, CifModel.class);
        CifModel mout = ((CifModel) outputModel);
        BpmnModel min = ((BpmnModel) inputModel);
        mout.setChoreoID(getChoreoID(min));
        setParticipants(min, mout);
        setAlphabet(min, mout);
        setStateMachine(min, mout);
        message("** Transformation achieved");
    }

    private void setParticipants(BpmnModel min, CifModel mout) throws IllegalModelException {
        // NEXT RELEASE check that participants always appear in the BPMN model (ie that one cannot have a task with at least one participant that is not declared) if not participants can be obtained while reading tasks
        try {
            PeerList pl = mout.getParticipants();
            for (Participant participant : min.getParticipants()) {
                Peer peer = new Peer();
                String peerId = getParticipantID(participant);
                String participantId = participant.getId();
                if (participantId == null) {
                    IllegalModelException e = new IllegalModelException("BPMN model is incorrect (a participant has no id)");
                    error(e.getMessage());
                    throw e;
                }
                peer.setPeerID(peerId);
                participants.put(participantId, peerId);
                pl.getPeer().add(peer);
            }
        } catch (IllegalModelException e) {
            error(e.getMessage());
            throw e;
        }
    }

    private int messageHashCode(Message message) {
        // used to check if a message has already been dealt with (to avoid duplicated in the message lists)
        // this is since we cannot change the CIF generated classes to implement hashcode + use a Set<Message> variable
        // so we do as in VerChor AlphabetElement (adaptor for a CifMessage)
        // NEXT RELEASE update the CIF model to avoid such legacy code burden
        int result = message.getMsgID().hashCode();
        result = 31 * result + message.getSender().hashCode();
        result = 31 * result + message.getReceiver().hashCode();
        return result;
    }

    private void setAlphabet(BpmnModel min, CifModel mout) throws IllegalModelException {
        // NEXT RELEASE update the CIF model to have generic messages (types) and sender/receiver(s) associated to choreography task rather than message type
        // for the time being, getting the alphabet could be done while computing the state machine but it has been separated to ease the above evolution
        try {
            List<FlowElement> fel = min.getFlowElements();
            MessageList ml = mout.getAlphabet();
            HashMap<Integer, Message> foundMessages = new HashMap<Integer, Message>();
            for (FlowElement fe : fel) {
                if (fe instanceof ChoreographyTask) {
                    ChoreographyTask choreographyTask = (ChoreographyTask) fe;
                    Message m = getMessageFromChoreographyTask(choreographyTask);
                    int hash = messageHashCode(m);
                    messages.put(choreographyTask.getId(), m.getMsgID()); // link to retrieve message from task
                    if (!foundMessages.containsKey(hash)) { // if this message does not already exists (i.e. same id, same sender, same receiver
                        ml.getMessageOrAction().add(m);
                        foundMessages.put(hash, m);
                    }
                }
            }
        } catch (IllegalModelException e) {
            error(e.getMessage());
            throw e;
        }

    }


    private void setStateMachine(BpmnModel min, CifModel mout) throws IllegalModelException {
        List<FlowElement> fel = min.getFlowElements();
        for (FlowElement fe : fel) {
            if (fe instanceof StartEvent) { // NEXT RELEASE make cleaner using Design Pattern
                transformStartEvent(min, mout, (StartEvent) fe);
            } else if (fe instanceof EndEvent) {
                transformEndEvent(min, mout, (EndEvent) fe);
            } else if (fe instanceof Gateway) {
                transformGateway(min, mout, (Gateway) fe);
            } else if (fe instanceof ChoreographyTask) {
                transformChoreographyTask(min, mout, (ChoreographyTask) fe);
            } else if (!(fe instanceof SequenceFlow)) {
                warning("BPMN model element not supported (" + fe + ")");
            }
        }
        if (!has_initial_state) {
            IllegalModelException e = new IllegalModelException("BPMN model is incorrect (no start event)");
            error(e.getMessage());
            throw e;
        }
        if (!has_final_state) {
            IllegalModelException e = new IllegalModelException("BPMN model is incorrect (no end event)");
            error(e.getMessage());
            throw e;
        }
    }

    private String getChoreoID(BpmnModel m) throws IllegalModelException {
        String rtr;
        try {
            rtr = m.getName();
            return rtr;
        } catch (IllegalModelException e) {
            warning(e.getMessage());
        }
        try {
            rtr = m.getId();
            return rtr;
        } catch (IllegalModelException e) {
            error(e.getMessage());
            throw e;
        }
    }

    private void transformStartEvent(BpmnModel min, CifModel mout, StartEvent startEvent) throws IllegalModelException {
        checkInStrict(startEvent, 0, true);
        checkOutStrict(startEvent, 1, true);
        InitialState initialState = new InitialState();
        String stateId = startEvent.getId(); // NEXT RELEASE extract method to get id and raise an exception if undefined (used several times in this class)
        if (stateId == null) {
            IllegalModelException e = new IllegalModelException("BPMN model is incorrect (a start event has no id)");
            error(e.getMessage());
            throw e;
        }
        initialState.setStateID(stateId);
        setOneSuccessor(initialState, startEvent);
        mout.getStateMachine().setInitial(initialState);
        if (has_initial_state) {
            IllegalModelException e = new IllegalModelException("BPMN model is incorrect (more than one start event)");
            error(e.getMessage());
            throw e;
        } else {
            has_initial_state = true;
        }
    }

    private void transformEndEvent(BpmnModel min, CifModel mout, EndEvent endEvent) throws IllegalModelException {
        checkInStrict(endEvent, 1, true);
        checkOutStrict(endEvent, 0, true);
        FinalState finalState = new FinalState();
        String stateId = endEvent.getId(); // NEXT RELEASE extract method to get id and raise an exception if undefined (used several times in this class)
        if (stateId == null) {
            IllegalModelException e = new IllegalModelException("BPMN model is incorrect (an end event has no id)");
            error(e.getMessage());
            throw e;
        }
        finalState.setStateID(stateId);
        mout.getStateMachine().getFinal().add(finalState);
        if (has_final_state) {
            this.warning("BPMN model has more than one end event");
        } else {
            has_final_state = true;
        }
    }

    private void transformGateway(BpmnModel min, CifModel mout, Gateway gateway) throws IllegalModelException {
        GatewayDirection direction = getDirection(gateway);
        if (direction.getValue() == GatewayDirection.CONVERGING_VALUE) {
            JoinState joinState = generateCIF_MergeGW(gateway);
            if (joinState != null) {
                mout.getStateMachine().getInteractionOrInternalActionOrSubsetJoin().add(joinState);
                joinState.setStateID(gateway.getId()); // NEXT RELEASE extract method to get id and raise an exception if undefined (used several times in this class)
                setOneSuccessor(joinState, gateway);
            }
        } else if (direction.getValue() == GatewayDirection.DIVERGING_VALUE) {
            SelectionState selectionState = generateCIF_SplitGW(gateway);
            if (selectionState != null) {
                mout.getStateMachine().getInteractionOrInternalActionOrSubsetJoin().add(selectionState);
                selectionState.setStateID(gateway.getId()); // NEXT RELEASE extract method to get id and raise an exception if undefined (used several times in this class)
                setAllSuccessors(selectionState, gateway);
            }
        } else {
            IllegalModelException e = new IllegalModelException("BPMN model incorrect (1-1, mixed or unknown type gateway " + gateway.getId() + " not supported)");
            error(e.getMessage());
            throw e;
        }
    }

    private void transformChoreographyTask(BpmnModel min, CifModel mout, ChoreographyTask choreographyTask) throws IllegalModelException {
        checkInStrict(choreographyTask, 1, true);
        checkOutStrict(choreographyTask, 1, true);
        InteractionState interactionState = new InteractionState();
        interactionState.setStateID(choreographyTask.getId());
        interactionState.setMsgID(messages.get(choreographyTask.getId()));
        setOneSuccessor(interactionState, choreographyTask);
        mout.getStateMachine().getInteractionOrInternalActionOrSubsetJoin().add(interactionState);
    }

    private boolean checkInStrict(FlowNode flowNode, int count, boolean warn) {
        boolean rtr = true;
        if (flowNode.getIncoming().size() != count) {
            rtr = false;
            if (warn) {
                warning("BPMN model is incorrect (" + flowNode + " should have only " + count + " predecessor(s))");
            }
        }
        return rtr;
    }

    private boolean checkOutStrict(FlowNode flowNode, int count, boolean warn) {
        boolean rtr = true;
        if (flowNode.getOutgoing().size() != count) {
            rtr = false;
            if (warn) {
                warning("BPMN model is incorrect (" + flowNode + " should have only " + count + " sucessor(s))");
            }
        }
        return rtr;
    }

    private boolean checkInMore(FlowNode flowNode, int count, boolean warn) {
        boolean rtr = true;
        if (flowNode.getIncoming().size() <= count) {
            rtr = false;
            if (warn) {
                warning("BPMN model is incorrect (" + flowNode + " should have more than " + count + " predecessor(s))");
            }
        }
        return rtr;
    }

    private boolean checkOutMore(FlowNode flowNode, int count, boolean warn) {
        boolean rtr = true;
        if (flowNode.getOutgoing().size() <= count) {
            rtr = false;
            if (warn) {
                warning("BPMN model is incorrect (" + flowNode + " should have more than " + count + " sucessor(s))");
            }
        }
        return rtr;
    }

    private GatewayDirection getDirection(Gateway gateway) {
        GatewayDirection direction;
        int given_dir;
        given_dir = gateway.getGatewayDirection().getValue();
        if (this.checkOutStrict(gateway, 1, false) && (this.checkInMore(gateway, 1, false))) {
            direction = GatewayDirection.CONVERGING;
            if (given_dir != direction.getValue())
                warning("BPMN model is incorrect (gateway " + gateway.getId() + " should be defined to be converging)");
        } else if (this.checkInStrict(gateway, 1, false) && (this.checkOutMore(gateway, 1, false))) {
            direction = org.eclipse.bpmn2.GatewayDirection.DIVERGING;
            if (given_dir != direction.getValue())
                this.warning("BPMN model is incorrect (gateway " + gateway.getId() + " should be defined to be diverging)");
        } else // 1-1 (no-op gateway) or n-m (unsupported)
            direction = org.eclipse.bpmn2.GatewayDirection.UNSPECIFIED;
        return direction;
    }

    private SelectionState generateCIF_SplitGW(Gateway gateway) throws IllegalModelException {
        // diverging ParallelGateway -> AllSelectState
        // diverging ExclusiveGateway -> ChoiceState
        // diverging InclusiveGateway -> SubsetSelectState
        // diverging ComplexGateway -> ??
        // diverging EventBasedGateWay -> ??
        // ?? -> DominatedChoiceState
        SelectionState rtr;
        if (gateway instanceof ParallelGateway)
            rtr = new AllSelectState();
        else if (gateway instanceof ExclusiveGateway)
            rtr = new ChoiceState();
        else if (gateway instanceof InclusiveGateway)
            rtr = new SubsetSelectState();
        else {
            IllegalModelException e = new IllegalModelException("BPMN model is incorrect (gateway " + gateway.getId() + " kind not supported (" + gateway.getClass().getName() + "))");
            error(e.getMessage());
            throw e;
        }
        return rtr;
    }

    private JoinState generateCIF_MergeGW(Gateway gateway) throws IllegalModelException {
        // converging ParallelGateway -> AllJoinState
        // converging ExclusiveGateway -> SimpleJoinState
        // converging InclusiveGateway -> SubsetJoinState
        // converging ComplexGateway -> ??
        // converging EventBasedGateWay -> ??
        JoinState rtr;
        if (gateway instanceof ParallelGateway)
            rtr = new AllJoinState();
        else if (gateway instanceof ExclusiveGateway)
            rtr = new SimpleJoinState();
        else if (gateway instanceof InclusiveGateway)
            rtr = new SubsetJoinState();
        else {
            IllegalModelException e = new IllegalModelException("BPMN model is incorrect (gateway " + gateway.getId() + " kind not supported (" + gateway.getClass().getName() + "))");
            error(e.getMessage());
            throw e;
        }
        return rtr;
    }

    private void setOneSuccessor(OneSuccState state, FlowNode flowNode) {
        SequenceFlow sequenceFlow;
        if (flowNode.getOutgoing().size() > 0) {
            // use only first successor
            sequenceFlow = flowNode.getOutgoing().get(0);
            state.getSuccessors().add(sequenceFlow.getTargetRef().getId()); // NEXT RELEASE check ID is not null
        }
    }

    private void setAllSuccessors(SeveralSuccState state, FlowNode flowNode) {
        for (SequenceFlow sequenceFlow : flowNode.getOutgoing()) {
            state.getSuccessors().add(sequenceFlow.getTargetRef().getId()); // NEXT RELEASE check ID is not null
        }
    }

    private String getParticipantID(Participant p) throws IllegalModelException {
        String rtr;
        if (p.getName() == null) {
            warning("BPMN model is incorrect (a peer has no name)");
            if (p.getId() == null) {
                IllegalModelException e = new IllegalModelException("BPMN model is incorrect (a peer has no id)");
                error(e.getMessage());
                throw e;
            } else {
                rtr = p.getId();
            }
        } else {
            rtr = p.getName();
        }
        return rtr;
    }

    private Message getMessageFromChoreographyTask(ChoreographyTask ct) throws IllegalModelException {
        Message rtr = new Message();
        String id;
        String contents;
        String sender;
        String receiver;
        // compute id and contents
        if (ct.getMessageFlowRef().size() == 0 || ct.getMessageFlowRef().get(0).getId() == null) { // no messages or wrong first message associated to the task -> use task information + issue warning
            warning(String.format("BPMN model has a task with no or incorrect first message flow (%s). The task information will be used.", ct.getId()));
            if (ct.getId() == null) {  // no task id. Nothing can be done
                IllegalModelException e = new IllegalModelException("BPMN model is incorrect. A choreography task has no id.");
                error(e.getMessage());
                throw e;
            }
            id = ct.getId();
            if (ct.getName() == null) { // no task name. Id will be used for contents
                warning(String.format("BPMN model has a task with no name (%s). The task id will be used.", ct.getId()));
                contents = id;
            } else {
                contents = ct.getName();
            }
        } else { // there are message flow. Use the first one.
            // NEXT RELEASE : support more than one message associated to the task
            org.eclipse.bpmn2.Message message = ct.getMessageFlowRef().get(0).getMessageRef();
            id = message.getId();
            if (message.getName() == null) { // no message name. Id will be used for contents
                contents = id;
            } else {
                contents = message.getName();
            }
        }
        // compute peers
        if (ct.getInitiatingParticipantRef() == null || ct.getParticipantRefs().size() != 2) {
            IllegalModelException e = new IllegalModelException(String.format("BPMN model is incorrect. A task (%s) should have exactly 1 sender and 1 receiver", ct.getId()));
            error(e.getMessage());
            throw e;
        }
        sender = participants.get(ct.getInitiatingParticipantRef().getId());
        List<Participant> partners = ct.getParticipantRefs();
        if (partners.get(0).getId().equals(ct.getInitiatingParticipantRef().getId()))
            receiver = participants.get(partners.get(1).getId());
        else
            receiver = participants.get(partners.get(0).getId());
        //
        rtr.setMsgID(id);
        rtr.setMessageContent(contents);
        rtr.setSender(sender);
        rtr.setReceiver(receiver);
        return rtr;
    }


    @Override
    public void about() {
        System.out.println(NAME + " " + VERSION);
    }

}
