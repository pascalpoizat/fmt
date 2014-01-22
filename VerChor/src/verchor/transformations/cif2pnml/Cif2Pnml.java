package verchor.transformations.cif2pnml;

import verchor.models.base.VerchorException;
import verchor.models.cif.CifFactory;
import verchor.models.pnml.PnmlFactory;
import verchor.transformations.base.ITransformer;

import java.io.IOException;

/**
 * Created by pascalpoizat on 10/01/2014.
 */
public class Cif2Pnml {
    public static void main(String[] args) {
        CifFactory cifFactory = CifFactory.getInstance();
        PnmlFactory pnmlFactory = PnmlFactory.getInstance();
        ITransformer trans = new Cif2PnmlTransformer(cifFactory, pnmlFactory);
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
        } catch (IOException e) {
            // DO NOTHING
        } catch (VerchorException e) {
            // DO NOTHING
        }
    }

}
