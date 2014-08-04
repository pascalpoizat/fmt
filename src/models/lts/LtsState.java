package models.lts;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pascalpoizat on 12/04/2014.
 */
public class LtsState {

    private String id;
    private Map<String, Object> attributes;

    public LtsState(String id) {
        this(id, null);
    }

    public LtsState(String id, Map<String, Object> attributes) {
        this.id = id;
        if (attributes == null) {
            this.attributes = new HashMap<String, Object>();
        } else {
            this.attributes = attributes;
        }
    }

    public String getId() {
        return id;
    }

    public Map<String, Object> getAttributes() { return attributes; }

    @Override
    public String toString() {
        // defaults to DOT format
        return this.write(new DotLtsWriter());
    }

    public String write(LtsWriter ltsWriter) {
        return ltsWriter.write(this);
    }
}
