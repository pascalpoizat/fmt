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

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A writer to dump LTS in DOT (Graphviz) format
 * Created by pascalpoizat on 04/08/2014.
 */
public class DotLtsWriter extends LtsWriter {

    public DotLtsWriter() {
    }

    @Override
    public String getSuffix() {
        return "dot";
    }

    @Override
    String modelToString(LtsLabel ltsLabel) {
        return ltsLabel.getLabel().toString();
    }

    @Override
    String modelToString(LtsState ltsState) {
        String rtr = "";
        rtr += String.format("\"%s\";", ltsState.getId());
        return rtr;
    }

    @Override
    String modelToString(LtsTransition ltsTransition) {
        String rtr = "";
        try {
            rtr += String.format("\"%s\" -> \"%s\" [label=\"%s\"]",
                    ltsTransition.getSource(),
                    ltsTransition.getTarget(),
                    ltsTransition.getLabel().modelToString(this));
            rtr += ";";
        }
        catch (IllegalResourceException e) {
            return null; // impossible
        }
        return rtr;
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
        if (ltsModel.getName() != null) {
            name = ltsModel.getName();
        }
        // build string for states
        try {
            // Java 1.7
            /*
            Class[] cArgs = new Class[1];
            cArgs[0] = LtsState.class;
            Method m = this.getClass().getDeclaredMethod("write",cArgs);
            lstates = Map.map(new ArrayList(ltsModel.getStates()), this, m);
            states_as_string = StringJoiner.join("\n", lstates);
            */
            // Java 1.8
            states_as_string = ltsModel.getStates().stream().map((x) -> x.modelToString(this)).collect(Collectors.joining("\n"));
        } catch (RuntimeException e) {
            return null; // impossible
        }
        // build string for transitions
        try {
            // Java 1.7
            /*
            Class[] cArgs = new Class[1];
            cArgs[0] = LtsTransition.class;
            Method m = this.getClass().getDeclaredMethod("write",cArgs);
            ltransitions = Map.map(new ArrayList(ltsModel.getTransitions()), this, m);
            transitions_as_string = StringJoiner.join("\n", ltransitions);
            */
            // Java 1.8
            transitions_as_string = ltsModel.getTransitions().stream().map((x) -> x.modelToString(this)).collect(Collectors.joining("\n"));
        } catch (RuntimeException e) {
            return null; // impossible
        }
        return String.format("digraph %s {\n%s\n%s\n}", name, states_as_string, transitions_as_string);
    }

}
