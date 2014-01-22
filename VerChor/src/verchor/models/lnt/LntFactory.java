package verchor.models.lnt;

import verchor.models.base.Model;
import verchor.models.base.ModelFactory;

/**
 * Created by pascalpoizat on 11/01/2014.
 */
public class LntFactory extends ModelFactory {

    private static LntFactory instance;

    private LntFactory() {
    }

    public static LntFactory getInstance() {
        if (instance == null)
            instance = new LntFactory();
        return instance;
    }

    public Model create() {
        Model model = new LntModel();
        return model;
    }

}
