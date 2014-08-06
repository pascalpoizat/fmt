package models.base;

import models.lts.LtsWriter;

import java.io.File;
import java.io.IOException;

/**
 * Created by pascalpoizat on 11/01/2014.
 */
public abstract class Model {

    private File resource;

    public Model() {
        resource = null;
    }

    // sets resource
    public void setResource(File resource) throws IllegalResourceException {
        if (resource == null) {
            throw new IllegalResourceException("No resource given");
        }
        this.resource = resource;
    }

    // gets resource
    public File getResource() {
        return resource;
    }

    // gets regular file suffix for resource
    public abstract String getSuffix();

    // loads model (just sets the flag, to be overriden + call to super)
    public void load() throws IOException, IllegalResourceException, IllegalModelException {
    }

    // dumps model
    public abstract void dump() throws IOException, IllegalResourceException;

    // writes model to a String
    public String modelToString(ModelWriter writer) throws IllegalResourceException {
        return writer.modelToString(this);
    }

    // writes model to a file
    public void modelToFile(ModelWriter writer) throws IllegalModelException, IllegalResourceException, IOException {
        writer.modelToFile(this);
    }

    // finalization (cleans up resources)
    public void cleanUp() {
        resource = null;
    }

}
