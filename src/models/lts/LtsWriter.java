package models.lts;

import models.base.IllegalModelException;
import models.base.IllegalResourceException;
import models.base.Model;
import models.base.ModelWriter;

import java.io.IOException;

/**
 * Created by pascalpoizat on 04/08/2014.
 */
public abstract class LtsWriter extends ModelWriter {
    // from ModelWriter

    // returns the suffix of the files the writer works with
    @Override
    public abstract String getSuffix();

    // writes model to a file
    @Override
    public abstract void modelToFile(Model model) throws IOException, IllegalResourceException, IllegalModelException;

    // writes model to a String
    @Override
    public abstract String modelToString(Model model) throws IllegalResourceException;

    // specific to LTS

    // writes a state to a String
    abstract String modelToString(LtsState ltsState) throws IllegalResourceException;

    // writes a transition to a String
    abstract String modelToString(LtsTransition ltsTransition) throws IllegalResourceException;

    // writes a label to a String
    abstract String modelToString(LtsLabel ltsLabel);
}
