package models.choreography.cif;

import models.base.ModelFactory;
import models.base.Model;

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
        return new CifModel();
    }

}
