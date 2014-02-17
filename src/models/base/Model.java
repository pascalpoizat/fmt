package models.base;

import java.io.File;
import java.io.IOException;

/**
 * Created by pascalpoizat on 11/01/2014.
 */
public abstract class Model {

    private File resource;
    private boolean is_loaded;

    public Model() {
        resource = null;
        is_loaded = false;
    }

    // sets resource
    public void setResource(File resource) throws IllegalResourceException {
        if(resource==null) {
            throw new IllegalResourceException("No resource given");
        }
        if (!resource.getName().endsWith("."+getSuffix())) {
            throw new IllegalResourceException("Wrong file suffix (should be "+getSuffix()+")");
        }
        this.resource = resource;
        is_loaded = false; // when the resource is changed the model should be re-loaded
    }

    // gets resource
    public File getResource() {
        return resource;
    }

    // checks if the model is loaded
    public boolean isLoaded() {
        return is_loaded;
    }

    // gets regular file suffix for resource
    public abstract String getSuffix();

    // loads model (just sets the flag, to be overriden + call to super)
    public void load() throws IOException, IllegalResourceException, IllegalModelException {
        is_loaded = true;
    }

    // dumps model
    public abstract void dump() throws IOException, IllegalResourceException;

    // finalization (cleans up resources)
    public void cleanUp() {
        resource = null;
        is_loaded = false;
    }

}
