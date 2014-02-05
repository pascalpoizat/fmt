package fr.lip6.move.transformations.cif2lnt;

import fr.lip6.move.pnml.pnmlcoremodel.Place;
import fr.lip6.move.pnml.ptnet.hlapi.*;
import fr.lip6.move.models.base.IllegalModelException;
import fr.lip6.move.models.lnt.LntFactory;
import fr.lip6.move.models.lnt.LntModel;
import fr.lip6.move.transformations.base.ATransformer;
import fr.lip6.move.models.cif.CifFactory;
import fr.lip6.move.models.cif.CifModel;

/**
 * Created by pascalpoizat on 30/01/2014.
 */
public class Cif2LntTransformer extends ATransformer {

    public static final String NAME = "cif2lnt";
    public static final String VERSION = "1.0";

    public Cif2LntTransformer(CifFactory cifFactory, LntFactory lntFactory) {
        super(cifFactory, lntFactory);
    }

    @Override
    public void transform() throws IllegalModelException {
        if (!in_model.isLoaded()) {
            IllegalModelException e = new IllegalModelException("Model is not loaded");
            error(e.getMessage());
            throw e;
        }
        // set fields of the output model
        if (in_model == null || out_model == null) {
            IllegalModelException e = new IllegalModelException("Model error");
            error(e.getMessage());
            throw e;
        }
        CifModel min = (CifModel) in_model;
        LntModel mout = (LntModel) out_model;
        // ONGOING
    }

    @Override
    public void about() {
        System.out.println(NAME + " " + VERSION);
    }

}
