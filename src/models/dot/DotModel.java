package models.dot;

import models.base.IllegalModelException;
import models.base.IllegalResourceException;
import models.base.Model;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pascalpoizat on 12/04/2014.
 */
public class DotModel extends Model {
    // only directed graphs for now on
    // Next Release : support more of the graphviz format

    private String name;
    private Map<String, DotState> states;
    private Map<String, DotTransition> transitions;

    public DotModel() {
        this(null);
    }

    public DotModel(String name) {
        super();
        this.name = name;
        this.states = new HashMap<String, DotState>();
        this.transitions = new HashMap<String, DotTransition>();
    }

    @Override
    public String getSuffix() {
        return "dot";
    }

    @Override
    public void dump() throws IOException, IllegalResourceException {
        FileWriter fw = new FileWriter(getResource().getAbsolutePath());
        if (fw == null) {
            throw new IllegalResourceException("Cannot open output resource");
        }
        fw.write(this.toString());
        fw.close();
    }

    @Override
    public void load() throws IOException, IllegalResourceException, IllegalModelException {
        // Next release : implement the loading from a dot file
    }

    @Override
    public String toString() {
        String rtr = "";
        rtr += "digraph ";
        if (name != null) {
            rtr += name;
        }
        rtr += " { \n";
        int i = 1;
        for (DotState state : states.values()) {
            rtr += state;
            if (i<states.size() || (transitions.size()>0)) {
                rtr += ";\n";
            }
        }
        i = 1;
        for (DotTransition transition : transitions.values()) {
            rtr += transition;
            if (i<transitions.size()) {
                rtr += ";\n";
            }
        }
        rtr += "}";
        return rtr;
    }

    @Override
    public void cleanUp() {
        states.clear();
        transitions.clear();
    }

    public DotState addState(String id, Map<String,String> attributes) {
        DotState rtr = new DotState(id,attributes);
        states.put(id,rtr);
        return rtr;
    }

    public DotTransition addTransition(String id, String source, String target, Map<String,String> attributes) {
        DotTransition rtr = new DotTransition(id,source,target,attributes);
        transitions.put(id,rtr);
        return rtr;
    }
}
