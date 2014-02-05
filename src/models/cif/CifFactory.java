package models.cif;

import models.base.Model;
import models.base.ModelFactory;

/**
 * Created by pascalpoizat on 11/01/2014.
 */
public class CifFactory extends ModelFactory {

    private static CifFactory instance;

    private CifFactory() {
    }

    public static CifFactory getInstance() {
        if (instance == null)
            instance = new CifFactory();
        return instance;
    }

    public Model create() {
        Model model = new CifModel();
        return model;
    }

}
