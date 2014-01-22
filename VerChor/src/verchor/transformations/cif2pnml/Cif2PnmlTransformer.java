package verchor.transformations.cif2pnml;

// java base

import fr.lip6.move.pnml.ptnet.PTMarking;
import fr.lip6.move.pnml.ptnet.Page;
import fr.lip6.move.pnml.ptnet.Place;
import fr.lip6.move.pnml.ptnet.PtnetFactory;
import verchor.models.base.IllegalModelException;
import verchor.models.cif.*;
import verchor.models.pnml.PnmlFactory;
import verchor.models.pnml.PnmlModel;
import verchor.transformations.base.ATransformer;

import java.util.List;

// jar Eclipse : org.eclipse.bpmn2_0.7.0.[...].jar
// verchor CIF format (classes generated with JAXB)

/**
 * Created by pascalpoizat on 10/01/2014.
 */
public class Cif2PnmlTransformer extends ATransformer {

    public static final String NAME = "cif2pnml";
    public static final String VERSION = "1.0";

    private PtnetFactory factory;
    private Page page;
    // TODO use own factory (inheriting one from the PNML framework) to create places and transitions with the correct ids
    private static final String prefix_place_message = "pm";
    private static final String prefix_place_state = "ps";

    public Cif2PnmlTransformer(CifFactory cifFactory, PnmlFactory pnmlFactory) {
        super(cifFactory, pnmlFactory);
        factory = PtnetFactory.eINSTANCE;
        page = null;
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
        CifModel min = (CifModel) in_model;
        PnmlModel mout = (PnmlModel) out_model;
        // create a single page
        page = factory.createPage();
        if (page == null) {
            IllegalModelException e = new IllegalModelException("PMNL model error (impossible to create page)");
            error(e.getMessage());
            throw e;
        }
        page.setId("main");
        mout.getPages().add(page);
        //
        createPlacesForMessages(min, mout);
        createPlacesForControlFlow(min, mout);
    }

    private Place createPlace(String prefix, String id) throws IllegalModelException {
        Place place = factory.createPlace();
        if (place == null) {
            IllegalModelException e = new IllegalModelException("PMNL model error (impossible to create place)");
            error(e.getMessage());
            throw e;
        }
        place.setId(prefix + "_" + id); // TODO ids must be unique and valid wrt PNML, to be checked
        page.getObjects().add(place);
        return place;
    }

    private void createPlacesForMessages(CifModel min, PnmlModel mout) throws IllegalModelException {
        Message message;
        message("Creating places for messages ...");
        for (Object object : min.getAlphabet().getMessageOrAction()) {
            message = (Message) object;
            createPlace(prefix_place_message, message.getMsgID());
        }
    }

    private void createPlacesForControlFlow(CifModel min, PnmlModel mout) throws IllegalModelException {
        Place p;
        // ONGOING
        message("Creating places for control flow ...");
        // deal with initial state // TODO these are not treated uniformly in CIF, should be
        createPlacesForInitialState(min);
        // deal with final states // TODO these are not treated uniformly in CIF, should be
        createPlacesForFinalStates(min);
        // deal with regular states
        for (BaseState state : min.getStateMachine().getInteractionOrInternalActionOrSubsetJoin()) {
            if (state instanceof AllSelectState) {  // TODO make cleaner using Design Pattern

            } else if (state instanceof AllJoinState) {

            } else if (state instanceof ChoiceState) {

            } else if (state instanceof SimpleJoinState) {

            } else if (state instanceof SubsetSelectState) {

            } else if (state instanceof SubsetJoinState) {

            } else if (state instanceof InteractionState) {

            } else if (state instanceof InitialState || state instanceof FinalState) {
                // should no happen in current version of CIF
                IllegalModelException e = new IllegalModelException("Should not happen");
                error(e.getMessage());
                throw e;
            } else {
                IllegalModelException e = new IllegalModelException("CIF element not supported (" + (state.getClass().toString()) + ")");
                error(e.getMessage());
                throw e;
            }
        }
    }

    private void createPlacesForFinalStates(CifModel min) throws IllegalModelException {
        List<FinalState> finalStates = min.getFinalStates();
        for (FinalState finalState : finalStates) {
            createPlace(prefix_place_state, finalState.getStateID());
        }
        // final states have no successors (no arcs to create)
    }

    private void createPlacesForInitialState(CifModel min) throws IllegalModelException {
        Place p;
        // create place for initial state
        InitialState initialState = min.getInitialState();
        p = createPlace(prefix_place_state, initialState.getStateID());
        PTMarking marking = factory.createPTMarking();
        marking.setText(1);
        p.setInitialMarking(marking);
        // ONGOING create arc for successor
    }

    @Override
    public void about() {
        System.out.println(NAME + " " + VERSION);
    }

}
