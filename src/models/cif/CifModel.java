package models.cif;

import models.base.IllegalModelException;
import models.base.IllegalResourceException;
import models.base.Model;
import models.cif.generated.*;

import javax.xml.bind.*;
import java.io.*;
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
    public boolean isLoaded() {
        return (model != null);
    }

    @Override
    public void load() throws IOException, IllegalResourceException, IllegalModelException {
        if (resource == null) {
            throw new IllegalResourceException("Input resource is not correctly set");
        }
        FileInputStream fis = new FileInputStream(resource);
        try {
            JAXBContext ctx = JAXBContext.newInstance(Choreography.class);
            Unmarshaller unmarshaller = ctx.createUnmarshaller();
            model = (Choreography) unmarshaller.unmarshal(fis);
        } catch (JAXBException e) {
            throw new IOException(e.getMessage());
        } finally {
            fis.close();
        }
    }

    @Override
    public void dump() throws IOException, IllegalResourceException {
        if (resource == null) {
            throw new IllegalResourceException("Output resource is not correctly set");
        }
        FileOutputStream fos = new FileOutputStream(resource);
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

    public BaseState getStateById(String id) throws IllegalModelException {  // TODO could be avoided with states in CIF model encoded using a HashTable
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
    public void finalize() {

    }

}

