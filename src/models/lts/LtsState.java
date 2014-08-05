package models.lts;

import models.base.IllegalResourceException;
import models.base.ModelWriter;

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

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String toString() {
        // defaults to DOT format
        try {
            return this.modelToString(new DotLtsWriter());
        } catch (RuntimeException e) {
            return null;
        } // impossible
    }

    public String modelToString(ModelWriter writer) throws RuntimeException {
        try {
            if (!(writer instanceof LtsWriter)) {
                throw new IllegalResourceException(String.format("Wrong kind of writer (%s), should be %s",
                        writer.getClass().toString(),
                        LtsWriter.class));
            }
            LtsWriter ltsWriter = (LtsWriter) writer;
            return ltsWriter.modelToString(this);
        } catch (IllegalResourceException e) {
            throw new RuntimeException(e); // BAD TRICK DUE TO Java 1.8 support for exceptions in map()
        }
    }
}
