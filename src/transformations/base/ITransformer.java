package transformations.base;

import models.base.IllegalModelException;
import models.base.IllegalResourceException;

import java.io.IOException;

/**
 * Created by pascalpoizat on 11/01/2014.
 */
public interface ITransformer {
    // sets the resources to operate on
    public void setResources(String path_to_input_resource) throws IllegalResourceException;

    // load input model
    public void load() throws IOException, IllegalResourceException, IllegalModelException;

    // perform transformation between input model and output model
    public void transform() throws IllegalModelException;

    // dump output model
    public void dump() throws IOException, IllegalResourceException;

    // finalize (cleans up models)
    public void cleanUp();

    // set the verbose mode
    public void setVerbose(boolean mode);

    // write a message
    public void message(String msg);

    // write an error
    public void error(String msg);

    // write a warning
    public void warning(String msg);

    // get information about the transformation
    public void about();
}
