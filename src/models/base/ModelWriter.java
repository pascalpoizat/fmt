package models.base;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by pascalpoizat on 05/08/2014.
 */
public abstract class ModelWriter {
    // returns the suffix of the files the writer works with
    public abstract String getSuffix();

    // writes model to a file
    // by default, uses modelToString(), can be modified by inheritance (eg for writers using EMF or XML)
    public void modelToFile(Model model) throws IOException, IllegalResourceException {
        if (model==null || model.getResource()==null)  {
            throw new IllegalResourceException("Model is not set up");
        }
        if (!model.getResource().getName().endsWith("." + getSuffix())) {
            throw new IllegalResourceException("Wrong file suffix (should be "+getSuffix()+")");
        }
        FileWriter fw = new FileWriter(model.getResource().getAbsolutePath());
        if (fw == null) {
            throw new IllegalResourceException("Cannot open output resource");
        }
        fw.write(modelToString(model));
        fw.close();
    }

    // writes model to a String
    public abstract String modelToString(Model model) throws IllegalResourceException;
}
