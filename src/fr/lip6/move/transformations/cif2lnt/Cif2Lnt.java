package fr.lip6.move.transformations.cif2lnt;

import fr.lip6.move.models.base.VerchorException;
import fr.lip6.move.models.cif.CifFactory;
import fr.lip6.move.models.lnt.LntFactory;
import fr.lip6.move.transformations.base.ITransformer;

import java.io.IOException;

/**
 * Created by pascalpoizat on 30/01/2014.
 */
public class Cif2Lnt {
    public static void main(String[] args) {
        CifFactory cifFactory = CifFactory.getInstance();
        LntFactory lntFactory = LntFactory.getInstance();
        ITransformer trans = new Cif2LntTransformer(cifFactory, lntFactory);
        trans.setVerbose(true);
        trans.about();
        if (args.length == 0) {
            trans.error("missing file argument");
            return;
        }
        try {
            trans.setResources(args[0]);
            trans.load();
            trans.transform();
            trans.dump();
            trans.finalize();
        } catch (IOException e) {
            // DO NOTHING
        } catch (VerchorException e) {
            // DO NOTHING
        }
    }

}
