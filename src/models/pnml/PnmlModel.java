package models.pnml;

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
import models.base.AbstractModel;
import models.base.IllegalModelException;
import models.base.IllegalResourceException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.*;

/**
 * Created by pascalpoizat on 11/01/2014.
 */
public class PnmlModel extends AbstractModel {

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
    public void load() {
        throw new NotImplementedException();
    }

    @Override
    public void dump() {
        throw new NotImplementedException();
    }

    @Override
    public void cleanUp() {
        try {
            ModelRepository.getInstance().destroyCurrentWorkspace();
        } catch (VoidRepositoryException e) {
        }
    }

    public PetriNetHLAPI getModel() {
        return model;
    }

    public PetriNetDocHLAPI getDoc() {
        return doc;
    }

    public void setModel(PetriNetHLAPI model) {
        this.model = model;
    }

    public void setDoc(PetriNetDocHLAPI doc) {
        this.doc = doc;
    }

}

