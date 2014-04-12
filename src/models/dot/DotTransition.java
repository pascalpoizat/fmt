package models.dot;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pascalpoizat on 12/04/2014.
 */
public class DotTransition {

    private String id;
    private Map<String, String> attributes;
    private String source_state_id;
    private String target_state_id;

    public DotTransition(String id, String source, String target) {
        this.id = id;
        this.source_state_id = source;
        this.target_state_id = target;
        attributes = new HashMap<String, String>();
    }

    public DotTransition(String id, String source, String target, Map<String, String> attributes) {
        this.id = id;
        this.source_state_id = source;
        this.target_state_id = target;
        if (attributes == null) {
            this.attributes = new HashMap<String, String>();
        } else {
            this.attributes = attributes;
        }
    }

    @Override
    public String toString() {
        String rtr = "";
        rtr += String.format("\"%s\" -> \"%s\"", source_state_id, target_state_id);
        if (attributes.size() > 0) {
            rtr += " [";
            int i = 1;
            for (String attribute : attributes.keySet()) {
                rtr += String.format("%s=\"%s\"", attribute, attributes.get(attribute));
                if (i < attributes.size()) {
                    rtr += ", ";
                }
            }
            rtr += "]";
        }
        rtr += ";\n";
        return rtr;
    }

}
