package verchor.transformations.cif2pnml;

import java.util.List;

import fr.lip6.move.pnml.pnmlcoremodel.Place;
import fr.lip6.move.pnml.ptnet.hlapi.ArcHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.NameHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.PageHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.PlaceHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.TransitionHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.PTMarkingHLAPI;
import verchor.transformations.base.ATransformer;
import verchor.models.cif.*;
import verchor.models.pnml.PnmlFactory;
import verchor.models.pnml.PnmlModel;
import verchor.models.base.IllegalModelException;

/**
 * Created by pascalpoizat on 10/01/2014.
 */
public class Cif2PnmlTransformer extends ATransformer {

    public static final String NAME = "cif2pnml";
    public static final String VERSION = "1.0";

    private PageHLAPI page;
    // TODO use own factory (inheriting one from the PNML framework) to create places and transitions with the correct ids
    private static final String separator = "_";
    private static final String prefix_place_message = "Pm";
    private static final String prefix_place_state = "Ps";
    private static final String prefix_place_before = "Pc";
    private static final String prefix_place_after = "Pr";
    private static final String prefix_transition_transition = "Tt";
    private static final String prefix_arc = "A";
    private static int next_arc = 0;

    public Cif2PnmlTransformer(CifFactory cifFactory, PnmlFactory pnmlFactory) {
        super(cifFactory, pnmlFactory);
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
        try {
            page = new PageHLAPI("main_page", new NameHLAPI("model"), null, (mout.getModel())); // create a single page
            createPlacesForMessages(min, mout);
            createPlacesForControlFlow(min, mout);
        } catch (Exception e) { // TODO deal with specific exceptions
            IllegalModelException e2 = new IllegalModelException("PNML model error (generation error)");
            error(e2.getMessage());
            e2.setStackTrace(e.getStackTrace());
            throw e2;
        }
    }

    private PlaceHLAPI createPlace(String prefix, String id) throws IllegalModelException {
        PlaceHLAPI place = null;
        try {
            place = new PlaceHLAPI(prefix + separator + id); // TODO ids must be unique and valid wrt PNML, to be checked
            place.setContainerPageHLAPI(page);
        } catch (Exception e) { // TODO deal with specific exceptions
            IllegalModelException e2 = new IllegalModelException("PNML model error (place generation error)");
            e2.setStackTrace(e.getStackTrace());
            throw e2;
        }
        return place;
    }

    private PlaceHLAPI createPlace(String prefix, String id, int marking) throws IllegalModelException {
        PlaceHLAPI place = createPlace(prefix, id);
        final PTMarkingHLAPI ptMarking = new PTMarkingHLAPI(marking, place);
        return place;
    }

    private TransitionHLAPI createTransition(String prefix, String id) throws IllegalModelException {
        TransitionHLAPI transition = null;
        try {
            transition = new TransitionHLAPI(prefix + separator + id); // TODO ids must be unique and valid wrt PNML, to be checked
            transition.setContainerPageHLAPI(page);
        } catch (Exception e) { // TODO deal with specific exceptions
            IllegalModelException e2 = new IllegalModelException("PNML model error (transition generation error)");
            e2.setStackTrace(e.getStackTrace());
            throw e2;
        }
        return transition;
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
            // final states have no successors (no arcs to create)
        }
    }

    private void createPlacesForInitialState(CifModel min) throws IllegalModelException {
        // create place for initial state
        InitialState initialState = min.getInitialState();
        PlaceHLAPI p = createPlace(prefix_place_state, initialState.getStateID(), 1);
        // create arc for successor
        if (initialState.getSuccessors().size() != 1) {
            IllegalModelException e = new IllegalModelException("CIF model is incorrect (state " + initialState.getStateID() + " should have exactly one successor)");
            error(e.getMessage());
            throw e;
        }
        encodeTransition(initialState, min.getStateById(initialState.getSuccessors().get(0)));
    }

    private void encodeTransition(BaseState source, BaseState target) throws IllegalModelException {
        PlaceHLAPI sourcePlace;
        PlaceHLAPI targetPlace;
        TransitionHLAPI transition;
        // create places
        sourcePlace = createPlace(prefix_place_before, source.getStateID() + separator + target.getStateID());
        targetPlace = createPlace(prefix_place_after, target.getStateID() + separator + source.getStateID());
        // create transition
        transition = createTransition(prefix_transition_transition, source.getStateID() + separator + target.getStateID());
        // create arcs
        try {
            ArcHLAPI arcIn = new ArcHLAPI(prefix_arc + (next_arc++), sourcePlace, transition, page);
            ArcHLAPI arcOut = new ArcHLAPI(prefix_arc + (next_arc++), transition, targetPlace, page);
        } catch (Exception e) {
            IllegalModelException e2 = new IllegalModelException("PNML model error (arc generation error)");
            e2.setStackTrace(e.getStackTrace());
            throw e2;
        }
    }


    @Override
    public void about() {
        System.out.println(NAME + " " + VERSION);
    }

}
