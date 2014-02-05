package fr.lip6.move.models.cif;

import fr.lip6.move.models.base.Model;
import fr.lip6.move.models.base.ModelFactory;

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
