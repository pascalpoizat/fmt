package models.base;

import java.io.File;
import java.io.IOException;

/**
 * Created by pascalpoizat on 05/02/2014.
 */
public abstract class ModelFactory implements AbstractModelFactory {
    @Override
    public abstract Model create();

    @Override
    public Model createFromFile(String filename) throws IOException, IllegalResourceException, IllegalModelException {
        Model model = create();
        model.setResource(new File(filename));
        model.load();
        return model;
    }
}
