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
 * Copyright (C) 2014  pascalpoizat
 * emails: pascal.poizat@lip6.fr
 */

package models.choreography.cif;

import models.base.AbstractModel;
import models.base.IllegalModelException;
import models.choreography.cif.generated.*;

import java.util.HashMap;
import java.util.List;

/**
 * Created by pascalpoizat on 11/01/2014.
 */
public class CifModel extends AbstractModel {

    public static final String BADMODEL = "CIF model is incorrect";
    private Choreography model;

    public CifModel() {
        super();
        model = new Choreography();
        final PeerList peerList = new PeerList();
        final MessageList messageList = new MessageList();
        final StateMachine stateMachine = new StateMachine();
        model.setParticipants(peerList);
        model.setAlphabet(messageList);
        model.setStateMachine(stateMachine);
    }

    public void setChoreoID(final String value) {
        if (model != null) {
            model.setChoreoID(value);
        }
    }

    public String getChoreoID() {
        if (model != null) {
            return model.getChoreoID();
        }
        else {
            return null;
        }
    }

    public PeerList getParticipants() throws IllegalModelException {
        if (model == null) {
            throw new IllegalModelException(BADMODEL);
        } else if (model.getParticipants() == null) {
            throw new IllegalModelException(BADMODEL +" (no participant list)");
        } else {
            return model.getParticipants();
        }
    }

    public MessageList getAlphabet() throws IllegalModelException {
        if (model == null) {
            throw new IllegalModelException(BADMODEL);
        } else if (model.getAlphabet() == null) {
            throw new IllegalModelException(BADMODEL +" (no alphabet)");
        } else {
            return model.getAlphabet();
        }
    }

    public HashMap<String, Message> getAlphabetAsMap() throws IllegalModelException {
        final HashMap<String, Message> rtr = new HashMap<>();
        Message message;
        if (model == null) {
            throw new IllegalModelException(BADMODEL);
        } else if (model.getAlphabet() == null) {
            throw new IllegalModelException(BADMODEL +" (no alphabet)");
        } else {
            for (final Object object : model.getAlphabet().getMessageOrAction()) {
                message = (Message) object;
                rtr.put(message.getMsgID(), message);
            }
        }
        return rtr;
    }

    public StateMachine getStateMachine() throws IllegalModelException {
        if (model == null) {
            throw new IllegalModelException(BADMODEL);
        } else if (model.getStateMachine() == null) {
            throw new IllegalModelException(BADMODEL +" (no state machine)");
        } else {
            return model.getStateMachine();
        }
    }

    public InitialState getInitialState() throws IllegalModelException {
        if (model == null || model.getStateMachine().getInitial() == null) {
            throw new IllegalModelException(BADMODEL +" (no initial state)");
        } else {
            return model.getStateMachine().getInitial();
        }
    }

    public List<FinalState> getFinalStates() throws IllegalModelException {
        if (model == null || model.getStateMachine().getFinal() == null || model.getStateMachine().getFinal().size() == 0) {
            throw new IllegalModelException(BADMODEL +" (no final state)");
        } else {
            return model.getStateMachine().getFinal();
        }
    }

    public BaseState getStateById(final String stateId) throws IllegalModelException {
        // could be avoided with states in CIF model encoded using a HashTable
        BaseState rtr = null;
        final List<BaseState> states = getStateMachine().getInteractionOrInternalActionOrSubsetJoin();
        for (final BaseState state : states) {
            if (state.getStateID().equals(stateId)) {
                rtr = state;
                break;
            }
        }
        return rtr;
    }

    @Override
    public void cleanUp() {
        model = new Choreography();
        final PeerList peerList = new PeerList();
        final MessageList messageList = new MessageList();
        final StateMachine stateMachine = new StateMachine();
        model.setParticipants(peerList);
        model.setAlphabet(messageList);
        model.setStateMachine(stateMachine);
        super.cleanUp();
    }

    public void setModel(Choreography model) {
        this.model = model;
    }

    public Choreography getModel() {
        return model;
    }

}

