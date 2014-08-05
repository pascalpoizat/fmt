package models.lts;

import models.base.IllegalResourceException;
import models.base.ModelWriter;

/**
 * Created by pascalpoizat on 05/08/2014.
 */
public class LtsLabel {

    private String label;

    public LtsLabel() {
        this("");
    }

    public LtsLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        // defaults to DOT format
        try {
            return this.modelToString(new DotLtsWriter());
        } catch (IllegalResourceException e) {
            return null;
        } // impossible
    }

    public String modelToString(ModelWriter writer) throws IllegalResourceException {
        if (!(writer instanceof LtsWriter)) {
            throw new IllegalResourceException(String.format("Wrong kind of writer (%s), should be %s",
                    writer.getClass().toString(),
                    LtsWriter.class));
        }
        LtsWriter ltsWriter = (LtsWriter) writer;
        return ltsWriter.modelToString(this);
    }

}
