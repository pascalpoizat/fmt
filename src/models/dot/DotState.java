package models.dot;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pascalpoizat on 12/04/2014.
 */
public class DotState {

    private String id;
    private Map<String, String> attributes;

    public DotState(String id) {
        this.id = id;
        attributes = new HashMap<String, String>();
    }

    public DotState(String id, Map<String, String> attributes) {
        this.id = id;
        if (attributes == null) {
            this.attributes = new HashMap<String, String>();
        } else {
            this.attributes = attributes;
        }
    }

    @Override
    public String toString() {
        String rtr = "";
        rtr += String.format("\"%s\"", id);
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
        return rtr;
    }
}
