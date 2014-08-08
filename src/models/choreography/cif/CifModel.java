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

import models.base.IllegalModelException;
import models.base.IllegalResourceException;
import models.base.Model;
import models.choreography.cif.generated.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.xml.bind.*;
import java.io.*;
import java.util.HashMap;
import java.util.List;

/**
 * Created by pascalpoizat on 11/01/2014.
 */
public class CifModel extends Model {

    private Choreography model;

    public CifModel() {
        super();
        model = new Choreography();
        PeerList pl = new PeerList();
        MessageList ml = new MessageList();
        StateMachine sm = new StateMachine();
        model.setParticipants(pl);
        model.setAlphabet(ml);
        model.setStateMachine(sm);
    }

    @Override
    public String getSuffix() {
        return "cif";
    }

    @Override
    public void load() throws IOException, IllegalResourceException, IllegalModelException {
        /*
        tbc, this should load the model and initialize its internal lists
        if (getResource() == null) {
            throw new IllegalResourceException("Input resource is not correctly set");
        }
        FileInputStream fis = new FileInputStream(getResource());
        try {
            JAXBContext ctx = JAXBContext.newInstance(Choreography.class);
            Unmarshaller unmarshaller = ctx.createUnmarshaller();
            model = (Choreography) unmarshaller.unmarshal(fis);
        } catch (JAXBException e) {
            throw new IOException(e.getMessage());
        } finally {
            fis.close();
        }
        super.load();
        */
        throw new NotImplementedException();
    }

    @Override
    public void dump() throws IOException, IllegalResourceException {
        if (getResource() == null) {
            throw new IllegalResourceException("Output resource is not correctly set");
        }
        FileOutputStream fos = new FileOutputStream(getResource());
        try {
            JAXBContext ctx = JAXBContext.newInstance(Choreography.class);
            Marshaller marshaller = ctx.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(model, fos);
        } catch (JAXBException e) {
            throw new IOException(e.getMessage());
        } finally {
            fos.close();
        }
    }

    public void setChoreoID(String value) {
        if (model != null) {
            model.setChoreoID(value);
        }
    }

    public PeerList getParticipants() throws IllegalModelException {
        if (model == null) {
            throw new IllegalModelException("CIF model is incorrect");
        } else if (model.getParticipants() == null) {
            throw new IllegalModelException("CIF model is incorrect (no participant list)");
        } else {
            return model.getParticipants();
        }
    }

    public MessageList getAlphabet() throws IllegalModelException {
        if (model == null) {
            throw new IllegalModelException("CIF model is incorrect");
        } else if (model.getAlphabet() == null) {
            throw new IllegalModelException("CIF model is incorrect (no alphabet)");
        } else {
            return model.getAlphabet();
        }
    }

    public HashMap<String, Message> getAlphabetAsMap() throws IllegalModelException {
        HashMap<String, Message> rtr = new HashMap<String, Message>();
        Message m;
        if (model == null) {
            throw new IllegalModelException("CIF model is incorrect");
        } else if (model.getAlphabet() == null) {
            throw new IllegalModelException("CIF model is incorrect (no alphabet)");
        } else {
            for (Object o : model.getAlphabet().getMessageOrAction()) {
                m = (Message) o;
                rtr.put(m.getMsgID(), m);
            }
        }
        return rtr;
    }

    public StateMachine getStateMachine() throws IllegalModelException {
        if (model == null) {
            throw new IllegalModelException("CIF model is incorrect");
        } else if (model.getStateMachine() == null) {
            throw new IllegalModelException("CIF model is incorrect (no state machine)");
        } else {
            return model.getStateMachine();
        }
    }

    public InitialState getInitialState() throws IllegalModelException {
        if (model == null || model.getStateMachine().getInitial() == null) {
            throw new IllegalModelException("CIF model is incorrect (no initial state)");
        } else {
            return model.getStateMachine().getInitial();
        }
    }

    public List<FinalState> getFinalStates() throws IllegalModelException {
        if (model == null || model.getStateMachine().getFinal() == null || model.getStateMachine().getFinal().size() == 0) {
            throw new IllegalModelException("CIF model is incorrect (no final state)");
        } else {
            return model.getStateMachine().getFinal();
        }
    }

    public BaseState getStateById(String id) throws IllegalModelException {
        // could be avoided with states in CIF model encoded using a HashTable
        BaseState rtr = null;
        List<BaseState> states = getStateMachine().getInteractionOrInternalActionOrSubsetJoin();
        for (BaseState state : states) {
            if (state.getStateID().equals(id)) {
                rtr = state;
                break;
            }
        }
        return rtr;
    }

    @Override
    public void cleanUp() {
        model = new Choreography();
        PeerList pl = new PeerList();
        MessageList ml = new MessageList();
        StateMachine sm = new StateMachine();
        model.setParticipants(pl);
        model.setAlphabet(ml);
        model.setStateMachine(sm);
        super.cleanUp();
    }

}

