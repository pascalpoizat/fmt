package verchor.models.svl;

import verchor.models.base.Model;
import verchor.models.base.ModelFactory;

/**
 * Created by pascalpoizat on 11/01/2014.
 */
public class SvlFactory extends ModelFactory {

    private static SvlFactory instance;

    private SvlFactory() {
    }

    public static SvlFactory getInstance() {
        if (instance == null)
            instance = new SvlFactory();
        return instance;
    }

    public Model create() {
        Model model = new SvlModel();
        return model;
    }

}
