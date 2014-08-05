package models.lts;

// Java 1.7
/*
import helpers.Map;
import helpers.StringJoiner;
import java.lang.reflect.Method;
import java.util.ArrayList;
*/

import models.base.IllegalModelException;
import models.base.IllegalResourceException;
import models.base.Model;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A writer to dump LTS in AUT (Aldebaran) format
 * format:
 * des(0,T,S)
 * (x,"l",y)
 * (x',"l'",y')
 * ...
 * where:
 * T is the number of transitions
 * S is the number of states
 * x, x', ... are source states of transitions (MUST BE INTEGERS)
 * y, y', ... are target states of transitions (MUST BE INTEGERS)
 * l, l', ... are labels of transitions
 * important:
 * some tools require that state ids (x,x',y,y',...) begin at 0 and go up to S-1 without any integer missing
 * this means state ids may have to be changed to support this (TODO)
 * Created by pascalpoizat on 04/08/2014.
 */
public class AutLtsWriter extends LtsWriter {

    private Map<String,Integer> state_mapping = new HashMap<String,Integer>();
    boolean state_mapping_is_built = false;

    public AutLtsWriter() {
    }

    @Override
    public String getSuffix() {
        return "aut";
    }

    @Override
    String modelToString(LtsLabel ltsLabel) {
        return ltsLabel.getLabel().toString();
    }

    @Override
    String modelToString(LtsState ltsState) {
        // noting to do
        return "";
    }

    @Override
    String modelToString(LtsTransition ltsTransition) throws IllegalResourceException {
        String rtr = "";
        Object object = "";
        LtsLabel label = null;
        if (ltsTransition.getAttributes().containsKey("label")) {
            object = ltsTransition.getAttributes().get("label");
            if(!(object instanceof LtsLabel)) {
                throw new IllegalResourceException("");
            }
            label = (LtsLabel)object;
            try {
                label.modelToString(this);
            }
            catch (IllegalResourceException e) {
                return null; // impossible
            }
        }
        if(!state_mapping_is_built) {
            throw new IllegalResourceException("Impossible to compute a String for the transition, mapping between states and integers has not been built");
        }
        rtr += String.format("(%s,\"%s\",%s)", state_mapping.get(ltsTransition.getSource()), label, state_mapping.get(ltsTransition.getTarget()));
        // state_mapping MUST have been built before
        // attributes are not supported in AUT format
        return rtr;
    }

    @Override
    public void modelToFile(Model model) throws IOException, IllegalResourceException, IllegalModelException {
        if (!model.getResource().getName().endsWith("." + getSuffix())) {
            throw new IllegalResourceException("Wrong file suffix (should be " + getSuffix() + ")");
        }
        FileWriter fw = new FileWriter(model.getResource().getAbsolutePath());
        if (fw == null) {
            throw new IllegalResourceException("Cannot open output resource");
        }
        fw.write(modelToString(model));
        fw.close();
    }

    @Override
    public String modelToString(Model model) throws IllegalResourceException {
        if (!(model instanceof LtsModel)) {
            throw new IllegalResourceException(String.format("Wrong kind of model (%s), should be %s",
                    model.getClass().toString(),
                    LtsModel.class.toString()));
        }
        LtsModel ltsModel = (LtsModel) model;
        String name = "";
        String states_as_string = "";
        String transitions_as_string = "";
        List<String> lstates = null;
        List<String> ltransitions = null;
        int nb_states = ltsModel.getStates().size();
        int nb_transitions = ltsModel.getTransitions().size();
        // build the mapping between state ids and integers (required by the AUT format)
        int i = 0;
        for(LtsState state : ((LtsModel) model).getStates()) {
            state_mapping.put(state.getId(),i++);
        }
        state_mapping_is_built = true;
        // build string for states
        // none in this format only transitions are saved
        // build string for transitions
        try {
            // Java 1.8
            transitions_as_string = ltsModel.getTransitions().stream().map((x) -> x.modelToString(this)).collect(Collectors.joining("\n"));
        } catch (RuntimeException e) {
            return null; // impossible
        }
        return String.format("des (0,%d,%d)\n%s\n", nb_transitions, nb_states, transitions_as_string);
    }

}
