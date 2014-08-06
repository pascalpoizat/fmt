package models.base;

import java.io.*;

/**
 * Created by pascalpoizat on 06/08/2014.
 */
public abstract class ModelReader {
    // returns the suffix of the files the writer works with
    public abstract String getSuffix();

    // reads model from a file
    // by default, uses modelFromString(), can be modified by inheritance (eg for readers using EMF or XML)
    public void modelFromFile(Model model) throws IOException, IllegalResourceException {
        if (model == null || model.getResource() == null) {
            throw new IllegalResourceException("");
        }
        if (!model.getResource().getName().endsWith("." + getSuffix())) {
            throw new IllegalResourceException("Wrong file suffix (should be " + getSuffix() + ")");
        }
        BufferedReader bufferedReader = new BufferedReader(new FileReader(model.getResource()));
        String s;
        StringBuilder builder = new StringBuilder();
        while ((s = bufferedReader.readLine()) != null) {
            builder.append(s);
        }
        modelFromString(model, builder.toString());
    }

    // reads model from a String
    public abstract void modelFromString(Model model, String string) throws IllegalResourceException;
}
