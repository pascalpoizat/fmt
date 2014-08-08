/**
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * <p>
 * {description}
 * Copyright (C) 2014  pascalpoizat
 * emails: pascal.poizat@lip6.fr
 */

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
import models.base.IllegalModelException;
import models.base.Model;
import models.base.IllegalResourceException;

import java.io.*;

/**
 * Created by pascalpoizat on 11/01/2014.
 */
public class PnmlModel extends Model {

    private PetriNetHLAPI model;
    private PetriNetDocHLAPI doc;

    public PnmlModel() {
        super();
        model = null; // NEXT RELEASE : construct base model
        doc = null; // NEXT RELEASE : construct base model
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
    public void load() throws IOException, IllegalResourceException, IllegalModelException {
        HLAPIClass rawModel;
        if (getResource() == null) {
            throw new IllegalResourceException("PNML resource is not set");
        }
        PnmlImport pnmlImport = new PnmlImport();
        try {
            rawModel = pnmlImport.importFile(getResource().getAbsolutePath());
        } catch (Exception e) {  // NEXT RELEASE deal with specific exceptions
            throw new IllegalResourceException("PNML resource is incorrect");
        }
        doc = (PetriNetDocHLAPI) rawModel;
        if (doc.getNetsHLAPI().size() == 0) {
            throw new IllegalResourceException("PNML resource is incorrect (no net in PNML doc)");
        }
        model = doc.getNetsHLAPI().get(0); // if more than one net, use the first one
        super.load();
    }

    @Override
    public void dump() throws IOException, IllegalResourceException {
        ModelRepository mr = ModelRepository.getInstance();
        mr.setPrettyPrintStatus(true);
        FileWriter fw = new FileWriter(getResource().getAbsolutePath());
        if (fw == null) {
            throw new IllegalResourceException("Cannot open output resource");
        }
        fw.write(doc.toPNML());
        fw.close();
    }

    @Override
    public void cleanUp() {
        try {
            ModelRepository.getInstance().destroyCurrentWorkspace();
        } catch (VoidRepositoryException e) {
        }
        model = null; // NEXT RELEASE : construct base model
        doc = null; // NEXT RELEASE : construct base model
        super.cleanUp();
    }

    public PetriNetHLAPI getModel() {
        return model;
    }

}

