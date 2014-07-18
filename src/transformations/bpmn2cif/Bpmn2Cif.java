package transformations.bpmn2cif;

import models.base.FmtException;
import transformations.base.ITransformer;
import models.choreography.bpmn.BpmnFactory;
import models.choreography.cif.CifFactory;

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
            trans.cleanUp();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (FmtException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (Throwable e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

}
