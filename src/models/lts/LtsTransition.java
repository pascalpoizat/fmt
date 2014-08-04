package models.lts;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pascalpoizat on 12/04/2014.
 */
public class LtsTransition {

    private String id;
    private Map<String, Object> attributes;
    private String source_state_id;
    private String target_state_id;

    public LtsTransition(String id, String source, String target) {
        this(id, source, target, null);
    }

    public LtsTransition(String id, String source, String target, Map<String, Object> attributes) {
        this.id = id;
        this.source_state_id = source;
        this.target_state_id = target;
        if (attributes == null) {
            this.attributes = new HashMap<String, Object>();
        } else {
            this.attributes = attributes;
        }
    }

    public String getId() {
        return id;
    }

    public String getSource() {
        return source_state_id;
    }

    public String getTarget() {
        return target_state_id;
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
