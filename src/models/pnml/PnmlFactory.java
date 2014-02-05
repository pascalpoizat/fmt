package models.pnml;

import models.base.Model;
import models.base.ModelFactory;

/**
 * Created by pascalpoizat on 11/01/2014.
 */
public class PnmlFactory extends ModelFactory {

    private static PnmlFactory instance;

    private PnmlFactory() {
    }

    public static PnmlFactory getInstance() {
        if (instance == null)
            instance = new PnmlFactory();
        return instance;
    }

    public Model create() {
        Model model = new PnmlModel();
        return model;
    }

}
