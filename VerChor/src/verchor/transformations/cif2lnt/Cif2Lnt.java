package verchor.transformations.cif2lnt;

import verchor.models.base.VerchorException;
import verchor.models.cif.CifFactory;
import verchor.models.lnt.LntFactory;
import verchor.transformations.base.ITransformer;
import verchor.transformations.cif2lnt.Cif2LntTransformer;

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
