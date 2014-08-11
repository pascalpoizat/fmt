/**
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License along
 *   with this program; if not, write to the Free Software Foundation, Inc.,
 *   51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 *   {description}
 *   Copyright (C) 2014  pascalpoizat
 *   emails: pascal.poizat@lip6.fr
 */
package models.choreography.cif;

import models.base.AbstractModel;
import models.base.IllegalModelException;
import models.base.IllegalResourceException;
import models.choreography.cif.generated.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DotCifWriter extends AbstractCifWriter {
    private static final String TASK_STYLE = "shape=record,style=\"filled,bold\",fixedsize=true,width=3,height=1,fillcolor=white,color=black";
    private static final String FINAL_STYLE = "shape=doublecircle,style=\"filled,bold\",fixedsize=true,width=0.5,fillcolor=black,color=black";
    private static final String INITIAL_STYLE = "shape=circle,style=\"filled,bold\",fixedsize=true,width=0.25,fillcolor=black,color=black";
    private static final String ALLSELECT_STYLE = "shape=circle,style=\"filled,bold\",fixedsize=true,width=0.5,fillcolor=white,color=black,fontsize=18,label=\"+\"";
    private static final String ALLJOIN_STYLE = "shape=circle,style=\"filled,bold\",fixedsize=true,width=0.5,fillcolor=white,color=black,fontsize=18,label=\"+\"";
    private static final String CHOICE_STYLE = "shape=circle,style=\"filled,bold\",fixedsize=true,width=0.5,fillcolor=white,color=black,fontsize=18,label=\"X\"";
    private static final String SIMPLEJOIN_STYLE = "shape=circle,style=\"filled,bold\",fixedsize=true,width=0.5,fillcolor=white,color=black,fontsize=18,label=\"X\"";
    private static final String DOMINATEDCHOICE_STYLE = "shape=circle,style=\"filled,bold\",fixedsize=true,width=0.5,fillcolor=white,color=black,fontsize=18,label=\"I\"";
    private static final String SUBSETSELECT_STYLE = "shape=circle,style=\"filled,bold\",fixedsize=true,width=0.5,fillcolor=white,color=black,fontsize=18,label=\"O\"";
    private static final String SUBSETJOIN_STYLE = "shape=circle,style=\"filled,bold\",fixedsize=true,width=0.5,fillcolor=white,color=black,fontsize=18,label=\"O\"";
    private static final String TRANSITION_STYLE = "";

    @Override
    public String getSuffix() {
        return "dot";
    }

    @Override
    public String modelToString(AbstractModel model) throws IllegalResourceException {
        if (!(model instanceof CifModel)) {
            throw new IllegalResourceException(String.format("Wrong model (%s), should be %s",
                    model.getClass().toString(),
                    CifModel.class.toString()));
        }
        CifModel cifModel = (CifModel) model;
        String rtr = "";
        rtr += String.format("digraph %s {\n" +
                        "rankdir=LR;\n" +
                        "fontname=\"Arial\";\n" +
                        "fontsize=\"14\";\n" +
                        "bgcolor=\"transparent\";\n" +
                        "concentrate=true;\n",
                cifModel.getChoreoID());
        try {
            // generate nodes
            List<BaseState> allStates = new ArrayList<BaseState>();
            rtr += modelToString(cifModel, cifModel.getInitialState());
            allStates.add(cifModel.getInitialState());
            for (FinalState finalState : cifModel.getFinalStates()) {
                rtr += modelToString(cifModel, finalState);
                allStates.add(finalState);
            }
            for (BaseState state : cifModel.getStateMachine().getInteractionOrInternalActionOrSubsetJoin()) {
                rtr += modelToString(cifModel, state);
                allStates.add(state);
            }
            // generates edges
            for (BaseState sourceState : allStates) {
                if (sourceState instanceof OneSuccState) {
                    rtr += modelToString(sourceState, ((OneSuccState) sourceState).getSuccessors());
                }
                else if (sourceState instanceof  SeveralSuccState) {
                    rtr += modelToString(sourceState, ((SeveralSuccState) sourceState).getSuccessors());
                }
            }
        } catch (IllegalModelException e) {
            e.printStackTrace();
        }
        rtr += "}\n";
        return rtr;
    }

    public String modelToString(BaseState source, List<String> targets) {
        String rtr = "";
        for (String target : targets) {
            rtr += String.format("%s -> %s [%s];\n", source.getStateID(), target, TRANSITION_STYLE);
        }
        return rtr;
    }

    public String modelToString(CifModel model, InitialState initialState) {
        return String.format("%s [%s," +
                "label=\"\"" +
                "];\n", initialState.getStateID(), INITIAL_STYLE);
    }

    public String modelToString(CifModel model, FinalState finalState) {
        return String.format("%s [%s," +
                "label=\"\"" +
                "];\n", finalState.getStateID(), FINAL_STYLE);
    }

    public String modelToString(CifModel model, BaseState state) throws IllegalResourceException {
        if (state instanceof InteractionState) {
            return modelToString(model, (InteractionState) state);
        }
        if (state instanceof JoinState) {
            return modelToString(model, (JoinState) state);
        }
        if (state instanceof SelectionState) {
            return modelToString(model, (SelectionState) state);
        }
        throw new IllegalResourceException(String.format("Element %s of class %s is not supported", state.getStateID(), state.getClass().toString()));
    }

    public String modelToString(CifModel model, InteractionState state) throws IllegalResourceException {
        String messageId = state.getMsgID();
        try {
            Map<String, Message> messages = model.getAlphabetAsMap();
            Message message = messages.get(messageId);
            String messageSender = message.getSender();
            String messageLabel = message.getMessageContent();
            String messageReceiver = message.getReceiver();
            String rtr = String.format("%s [%s," +
                    "label=\"%s | %s | %s\"" +
                    "];\n", state.getStateID(), TASK_STYLE, messageSender, messageLabel, messageReceiver);
            return rtr;
        } catch (IllegalModelException e) {
            throw new IllegalResourceException(e.getMessage());
        }
    }

    public String modelToString(CifModel model, JoinState state) throws IllegalResourceException {
        if (state instanceof AllJoinState) {
            String rtr = String.format("%s [%s];\n", state.getStateID(), ALLJOIN_STYLE);
            return rtr;
        }
        if (state instanceof SimpleJoinState) {
            String rtr = String.format("%s [%s];\n", state.getStateID(), SIMPLEJOIN_STYLE);
            return rtr;
        }
        if (state instanceof SubsetJoinState) {
            String rtr = String.format("%s [%s];\n", state.getStateID(), SUBSETJOIN_STYLE);
            return rtr;
        }
        throw new IllegalResourceException(String.format("Element %s of class %s is not supported", state.getStateID(), state.getClass().toString()));
    }

    public String modelToString(CifModel model, SelectionState state) throws IllegalResourceException {
        if (state instanceof AllSelectState) {
            String rtr = String.format("%s [%s];\n", state.getStateID(), ALLSELECT_STYLE);
            return rtr;
        }
        if (state instanceof ChoiceState) {
            String rtr = String.format("%s [%s];\n", state.getStateID(), CHOICE_STYLE);
            return rtr;
        }
        if (state instanceof DominatedChoiceState) {
            String rtr = String.format("%s [%s];\n", state.getStateID(), DOMINATEDCHOICE_STYLE);
            return rtr;
        }
        if (state instanceof SubsetSelectState) {
            String rtr = String.format("%s [%s];\n", state.getStateID(), SUBSETSELECT_STYLE);
            return rtr;
        }
        throw new IllegalResourceException(String.format("Element %s of class %s is not supported", state.getStateID(), state.getClass().toString()));
    }

}
