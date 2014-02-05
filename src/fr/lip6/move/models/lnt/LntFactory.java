package fr.lip6.move.models.lnt;

import fr.lip6.move.models.base.Model;
import fr.lip6.move.models.base.ModelFactory;

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
