package models.base;

import java.io.IOException;

/**
 * Created by pascalpoizat on 05/08/2014.
 */
public abstract class ModelWriter {
    // returns the suffix of the files the writer works with
    public abstract String getSuffix();

    // writes model to a file
    public abstract void modelToFile(Model model) throws IOException, IllegalResourceException, IllegalModelException;

    // writes model to a String
    public abstract String modelToString(Model model) throws IllegalResourceException;
}
