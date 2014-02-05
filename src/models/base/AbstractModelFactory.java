package models.base;

import java.io.IOException;

/**
 * Created by pascalpoizat on 11/01/2014.
 */
public interface AbstractModelFactory {
    public abstract Model create(); // creates an empty model (using the empty constructor

    public abstract Model createFromFile(String filename) throws IOException, IllegalResourceException, IllegalModelException; // creates a model and loads it from a file
}
