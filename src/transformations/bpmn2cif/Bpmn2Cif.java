package transformations.bpmn2cif;

import models.base.FmtException;
import transformations.base.ITransformer;
import models.bpmn.BpmnFactory;
import models.cif.CifFactory;

import java.io.IOException;

/**
 * Created by pascalpoizat on 10/01/2014.
 */
public class Bpmn2Cif {
    public static void main(String[] args) {
        BpmnFactory bpmnFactory = BpmnFactory.getInstance();
        CifFactory cifFactory = CifFactory.getInstance();
        ITransformer trans = new Bpmn2CifTransformer(bpmnFactory, cifFactory);
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
        } catch (FmtException e) {
            // DO NOTHING
        }
    }

}