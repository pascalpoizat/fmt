package models.lts;

import models.base.IllegalResourceException;
import models.base.Model;
import models.base.ModelReader;

/**
 * Created by pascalpoizat on 06/08/2014.
 */
public abstract class LtsReader extends ModelReader {
    // from ModelReader

    // returns the suffix of the files the writer works with
    @Override
    public abstract String getSuffix();

    // reads model from a String
    @Override
    public abstract void modelFromString(Model model, String string) throws IllegalResourceException;

    // specific to LTS

}
