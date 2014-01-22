package verchor.models.base;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by pascalpoizat on 11/01/2014.
 */
public abstract class Model {

    protected File resource;

    public Model() {
        resource = null;
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
    }

    // checks if the model is loaded
    public abstract boolean isLoaded();

    // gets regular file suffix for resource
    public abstract String getSuffix();

    // loads model
    public abstract void load() throws IOException, IllegalResourceException, IllegalModelException;

    // dumps model
    public abstract void dump() throws IOException, IllegalResourceException;

}
