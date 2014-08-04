package models.lts;

// Java 1.7
/*
import helpers.Map;
import helpers.StringJoiner;
import java.lang.reflect.Method;
import java.util.ArrayList;
*/
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by pascalpoizat on 04/08/2014.
 */
public class DotLtsWriter extends LtsWriter {

    public DotLtsWriter() {
    }

    @Override
    public String write(LtsState ltsState) {
        String rtr = "";
        rtr += String.format("\"%s\"", ltsState.getId());
        if (ltsState.getAttributes().size() > 0) {
            rtr += " [";
            int i = 1;
            for (String attribute : ltsState.getAttributes().keySet()) {
                rtr += String.format("%s=\"%s\"", attribute, ltsState.getAttributes().get(attribute));
                if (i < ltsState.getAttributes().size()) {
                    rtr += ", ";
                }
                i++;
            }
            rtr += "]";
        }
        rtr += ";";
        return rtr;
    }

    @Override
    public String write(LtsTransition ltsTransition) {
        String rtr = "";
        rtr += String.format("\"%s\" -> \"%s\"", ltsTransition.getSource(), ltsTransition.getTarget());
        if (ltsTransition.getAttributes().size() > 0) {
            rtr += " [";
            int i = 1;
            for (String attribute : ltsTransition.getAttributes().keySet()) {
                rtr += String.format("%s=\"%s\"", attribute, ltsTransition.getAttributes().get(attribute));
                if (i < ltsTransition.getAttributes().size()) {
                    rtr += ", ";
                }
                i++;
            }
            rtr += "]";
        }
        rtr += ";";
        return rtr;
    }

    @Override
    public String write(LtsModel ltsModel) {
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
            states_as_string = ltsModel.getStates().stream().map((x) -> x.write(this)).collect(Collectors.joining("\n"));
        } catch (Exception e) {
            System.out.println("problem: " + e.getMessage());
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
            transitions_as_string = ltsModel.getTransitions().stream().map((x) -> x.write(this)).collect(Collectors.joining("\n"));
        } catch (Exception e) {
            System.out.println("problem: " + e.getMessage());
        }
        return String.format("digraph %s {\n%s\n%s\n}", name, states_as_string, transitions_as_string);
    }

}
