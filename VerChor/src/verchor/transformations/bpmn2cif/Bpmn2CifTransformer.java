package verchor.transformations.bpmn2cif;

// java base

import java.util.HashMap;
import java.util.List;

// jar Eclipse : org.eclipse.bpmn2_0.7.0.[...].jar
import org.eclipse.bpmn2.*;

// verchor CIF format (classes generated with JAXB)
import verchor.models.base.IllegalModelException;
import verchor.models.cif.Message;
import verchor.transformations.base.ATransformer;
import verchor.models.cif.*;
import verchor.models.bpmn.BpmnModel;
import verchor.models.bpmn.BpmnFactory;

/**
 * Created by pascalpoizat on 10/01/2014.
 */
public class Bpmn2CifTransformer extends ATransformer {

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
    public static final String VERSION = "1.0";

    HashMap<String, String> participants;   // participant id in bpmn model -> participant id in cif model
    HashMap<String, String> messages;       // message id in bpmn model -> message id in cif model

    boolean has_initial_state;
    boolean has_final_state;

    public Bpmn2CifTransformer(BpmnFactory bpmnFactory, CifFactory cifFactory) {
        super(bpmnFactory, cifFactory);
        participants = new HashMap<String, String>();
        messages = new HashMap<String, String>();
        has_initial_state = false;
        has_final_state = false;
    }

    @Override
    public void transform() throws IllegalModelException {
        if (!in_model.isLoaded()) {
            IllegalModelException e = new IllegalModelException("Model is not loaded");
            error(e.getMessage());
            throw e;
        }
        // set fields of the output model
        if (in_model == null || out_model == null) {
            IllegalModelException e = new IllegalModelException("Model error");
            error(e.getMessage());
            throw e;
        }
        CifModel mout = ((CifModel) out_model);
        BpmnModel min = ((BpmnModel) in_model);
        mout.setChoreoID(getChoreoID(min));
        setParticipants(min, mout);
        setAlphabet(min, mout);
        setStateMachine(min, mout);
    }

    private void setParticipants(BpmnModel min, CifModel mout) throws IllegalModelException {
        // TODO check that participants always appear in the BPMN model (ie that one cannot have a task with at least one participant that is not declared) if not participants can be obtained while reading tasks
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

    private void setAlphabet(BpmnModel min, CifModel mout) throws IllegalModelException {
        // TODO require that messages are used in conjunction with choreography tasks and use the list of message types instead of the choreography tasks ids to build the CIF alphabet
        // TODO update the CIF model to have generic messages (types) and sender/receiver(s) associated to choreography task rather than message type
        // for the time being, getting the alphabet could be done while computing the state machine but it has been separated to ease the above evolution
        try {
            List<FlowElement> fel = min.getFlowElements();
            MessageList ml = mout.getAlphabet();
            for (FlowElement fe : fel) {
                if (fe instanceof ChoreographyTask) {
                    ChoreographyTask choreographyTask = (ChoreographyTask) fe;
                    Message m = new Message();
                    String id = choreographyTask.getId();
                    if (id == null) {
                        IllegalModelException e = new IllegalModelException("BPMN model is incorrect (a choreography task has no id)");
                        error(e.getMessage());
                        throw e;
                    }
                    m.setMsgID(choreographyTask.getId());
                    String messageId = getMessageID(choreographyTask);
                    m.setMessageContent(messageId);
                    messages.put(id, messageId);
                    if (choreographyTask.getInitiatingParticipantRef() == null) {
                        IllegalModelException e = new IllegalModelException("BPMN model is incorrect (choreography task " + choreographyTask.getId() + " has undefined initiating participant)");
                        error(e.getMessage());
                        throw e;
                    }
                    m.setSender(participants.get(choreographyTask.getInitiatingParticipantRef().getId()));
                    List<Participant> partners = choreographyTask.getParticipantRefs();
                    if (partners.size() != 2) {
                        IllegalModelException e = new IllegalModelException("BPMN model is incorrect (choreography task " + fe.getId() + " has not 1 sender and 1 receiver)");
                        error(e.getMessage());
                        throw e;
                    }
                    if (partners.get(0).getId().equals(choreographyTask.getInitiatingParticipantRef().getId()))
                        m.setReceiver(participants.get(partners.get(1).getId()));
                    else
                        m.setReceiver(participants.get(partners.get(0).getId()));
                    ml.getMessageOrAction().add(m);
                }
            }
        } catch (
                IllegalModelException e
                )

        {
            error(e.getMessage());
            throw e;
        }

    }


    private void setStateMachine(BpmnModel min, CifModel mout) throws IllegalModelException {
        List<FlowElement> fel = min.getFlowElements();
        for (FlowElement fe : fel) {
            if (fe instanceof StartEvent) { // TODO make cleaner using Design Pattern
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
        String stateId = startEvent.getId(); // TODO extract method to get id and raise an exception if undefined (used several times in this class)
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
        String stateId = endEvent.getId(); // TODO extract method to get id and raise an exception if undefined (used several times in this class)
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
                joinState.setStateID(gateway.getId()); // TODO extract method to get id and raise an exception if undefined (used several times in this class)
                setOneSuccessor(joinState, gateway);
            }
        } else if (direction.getValue() == GatewayDirection.DIVERGING_VALUE) {
            SelectionState selectionState = generateCIF_SplitGW(gateway);
            if (selectionState != null) {
                mout.getStateMachine().getInteractionOrInternalActionOrSubsetJoin().add(selectionState);
                selectionState.setStateID(gateway.getId()); // TODO extract method to get id and raise an exception if undefined (used several times in this class)
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
            state.getSuccessors().add(sequenceFlow.getTargetRef().getId()); // TODO check ID is not null
        }
    }

    private void setAllSuccessors(SeveralSuccState state, FlowNode flowNode) {
        for (SequenceFlow sequenceFlow : flowNode.getOutgoing()) {
            state.getSuccessors().add(sequenceFlow.getTargetRef().getId()); // TODO check ID is not null
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

    private String getMessageID(ChoreographyTask ct) throws IllegalModelException {
        String rtr;
        if (ct.getName() == null) {
            warning("BPMN model is incorrect (a choreography task has no name)");
            if (ct.getId() == null) {
                IllegalModelException e = new IllegalModelException("BPMN model is incorrect (a choreography task has no id");
                error(e.getMessage());
                throw e;
            } else {
                rtr = ct.getId();
            }
        } else {
            rtr = ct.getName();
        }
        return rtr;
    }


    @Override
    public void about() {
        System.out.println(NAME + " " + VERSION);
    }

}
