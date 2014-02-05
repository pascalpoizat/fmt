package transformations.cif2pnml;

import models.base.VerchorException;
import models.cif.CifFactory;
import models.pnml.PnmlFactory;
import transformations.base.ITransformer;

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
            trans.finalize();
        } catch (IOException e) {
            // DO NOTHING
        } catch (VerchorException e) {
            // DO NOTHING
        }
    }

}
