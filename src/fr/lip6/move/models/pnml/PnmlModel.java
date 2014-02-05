package fr.lip6.move.models.pnml;

// import fr.lip6.fr.lip6.move.pnml.framework.general.PnmlExport;
import fr.lip6.move.pnml.framework.utils.exception.InvalidIDException;
import fr.lip6.move.pnml.framework.utils.exception.VoidRepositoryException;
import fr.lip6.move.pnml.ptnet.hlapi.PetriNetHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.PetriNetDocHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.PNTypeHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.NameHLAPI;
import fr.lip6.move.pnml.framework.hlapi.HLAPIClass;
import fr.lip6.move.pnml.framework.general.PnmlImport;
import fr.lip6.move.pnml.framework.utils.ModelRepository;
import fr.lip6.move.models.base.Model;
import fr.lip6.move.models.base.IllegalResourceException;

import java.io.*;

/**
 * Created by pascalpoizat on 11/01/2014.
 */
public class PnmlModel extends Model {

    private PetriNetHLAPI model;
    private PetriNetDocHLAPI doc;

    public PnmlModel() {
        super();
        model = null;
        doc = null;
        try {
            ModelRepository.getInstance().createDocumentWorkspace("main_workspace");
            doc = new PetriNetDocHLAPI();
            model = new PetriNetHLAPI("main_net", PNTypeHLAPI.PTNET, new NameHLAPI("main_net"), doc);
        } catch (InvalidIDException e) {
        } catch (VoidRepositoryException e) {
        }
    }

    @Override
    public String getSuffix() {
        return "pnml";
    }

    @Override
    public boolean isLoaded() {
        return (model != null);
    }

    @Override
    public void load() throws IllegalResourceException {
        HLAPIClass rawModel;
        if (resource == null) {
            throw new IllegalResourceException("PNML resource is not set");
        }
        PnmlImport pnmlImport = new PnmlImport();
        try {
            rawModel = pnmlImport.importFile(resource.getAbsolutePath());
        } catch (Exception e) {  // TODO deal with specific exceptions
            throw new IllegalResourceException("PNML resource is incorrect");
        }
        doc = (PetriNetDocHLAPI) rawModel;
        if (doc.getNetsHLAPI().size() == 0) {
            throw new IllegalResourceException("PNML resource is incorrect (no net in PNML doc)");
        }
        model = doc.getNetsHLAPI().get(0); // if more than one net, use the first one
    }

    @Override
    public void dump() throws IOException, IllegalResourceException {
        ModelRepository mr = ModelRepository.getInstance();
        mr.setPrettyPrintStatus(true);
        FileWriter fw = new FileWriter(resource.getAbsolutePath());
        if (fw == null) {
            throw new IllegalResourceException("Cannot open output resource");
        }
        fw.write(doc.toPNML());
        fw.close();
        // The following does not work (issue with Apache Log4J
//        PnmlExport pnmlExport = new PnmlExport();
//        try {
//            pnmlExport.exportObject(doc, resource.getAbsolutePath());
//        } catch (Exception e) { // TODO deal with specific exceptions
//            throw new IllegalResourceException("PNML error (impossible to save resource)");
//        }
    }

    @Override
    public void finalize() {
        try {
            ModelRepository.getInstance().destroyCurrentWorkspace();
        } catch (VoidRepositoryException e) {
        }
    }

    public PetriNetHLAPI getModel() {
        return model;
    }

}

