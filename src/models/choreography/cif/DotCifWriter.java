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
 *   Copyright (C) 2014 Pascal Poizat (@pascalpoizat)
 *   emails: pascal.poizat@lip6.fr
 */
package models.choreography.cif;

import models.base.*;
import models.choreography.cif.generated.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DotCifWriter extends AbstractStringModelWriter {
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
    public String modelToString(AbstractModel model) throws IllegalResourceException, IllegalModelException {
        checkModel(model, CifModel.class);
        CifModel cifModel = (CifModel) model;
        String rtr = "";
        rtr += String.format("digraph %s {\n" +
                        "rankdir=LR;\n" +
                        "fontname=\"Arial\";\n" +
                        "fontsize=\"14\";\n" +
                        "bgcolor=\"transparent\";\n" +
                        "concentrate=true;\n",
                        normalizeId(cifModel.getChoreoID()));
        try {
            // generate nodes
            List<BaseState> allStates = new ArrayList<>();
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
                } else if (sourceState instanceof SeveralSuccState) {
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
            rtr += String.format("%s -> %s [%s];\n", normalizeId(source.getStateID()), normalizeId(target), TRANSITION_STYLE);
        }
        return rtr;
    }

    public String modelToString(CifModel model, InitialState initialState) {
        return String.format("%s [%s," +
                "label=\"\"" +
                "];\n", normalizeId(initialState.getStateID()), INITIAL_STYLE);
    }

    public String modelToString(CifModel model, FinalState finalState) {
        return String.format("%s [%s," +
                "label=\"\"" +
                "];\n", normalizeId(finalState.getStateID()), FINAL_STYLE);
    }

    public String modelToString(CifModel model, BaseState state) throws IllegalModelException {
        if (state instanceof InteractionState) {
            return modelToString(model, (InteractionState) state);
        }
        if (state instanceof JoinState) {
            return modelToString(model, (JoinState) state);
        }
        if (state instanceof SelectionState) {
            return modelToString(model, (SelectionState) state);
        }
        throw new IllegalModelException(String.format("Element %s of class %s is not supported", state.getStateID(), state.getClass().toString()));
    }

    public String modelToString(CifModel model, InteractionState state) throws IllegalModelException {
        String messageId = state.getMsgID();
        Map<String, Message> messages = model.getAlphabetAsMap();
        Message message = messages.get(messageId);
        String messageSender = message.getSender();
        String messageLabel = message.getMessageContent();
        String messageReceiver = message.getReceiver();
        return String.format("%s [%s," +
                "label=\"%s | %s | %s\"" +
                "];\n", normalizeId(state.getStateID()), TASK_STYLE, messageSender, messageLabel, messageReceiver);
    }

    public String modelToString(CifModel model, JoinState state) throws IllegalModelException {
        if (state instanceof AllJoinState) {
            return String.format("%s [%s];\n", normalizeId(state.getStateID()), ALLJOIN_STYLE);
        }
        if (state instanceof SimpleJoinState) {
            return String.format("%s [%s];\n", normalizeId(state.getStateID()), SIMPLEJOIN_STYLE);
        }
        if (state instanceof SubsetJoinState) {
            return String.format("%s [%s];\n", normalizeId(state.getStateID()), SUBSETJOIN_STYLE);
        }
        throw new IllegalModelException(String.format("Element %s of class %s is not supported", state.getStateID(), state.getClass().toString()));
    }

    public String modelToString(CifModel model, SelectionState state) throws IllegalModelException {
        if (state instanceof AllSelectState) {
            return String.format("%s [%s];\n", normalizeId(state.getStateID()), ALLSELECT_STYLE);
        }
        if (state instanceof ChoiceState) {
            return String.format("%s [%s];\n", normalizeId(state.getStateID()), CHOICE_STYLE);
        }
        if (state instanceof DominatedChoiceState) {
            return String.format("%s [%s];\n", normalizeId(state.getStateID()), DOMINATEDCHOICE_STYLE);
        }
        if (state instanceof SubsetSelectState) {
            return String.format("%s [%s];\n", normalizeId(state.getStateID()), SUBSETSELECT_STYLE);
        }
        throw new IllegalModelException(String.format("Element %s of class %s is not supported", state.getStateID(), state.getClass().toString()));
    }

    /**
     * normalizes ids so that dot accepts them, eg replacing spaces by underscores
     * @param id the raw id to normalize
     * @return the normalized id
     */
    private static String normalizeId(String id) {
        String rtr;
        rtr = id.replace(" ", "_");
        return rtr;
    }

}
