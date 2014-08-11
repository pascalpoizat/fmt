/**
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License along
 *   with this program; if not, write to the Free Software Foundation, Inc.,
 *   51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 *   {description}
 *   Copyright (C) 2014  pascalpoizat
 *   emails: pascal.poizat@lip6.fr
 */

package models.pnml;

import fr.lip6.move.pnml.framework.general.PnmlImport;
import fr.lip6.move.pnml.framework.hlapi.HLAPIClass;
import fr.lip6.move.pnml.framework.utils.exception.*;
import fr.lip6.move.pnml.ptnet.hlapi.PetriNetDocHLAPI;
import models.base.AbstractModel;
import models.base.AbstractModelReader;
import models.base.IllegalModelException;
import models.base.IllegalResourceException;

import java.io.IOException;

public class PnmlPnmlReader extends AbstractModelReader {
    @Override
    public String getSuffix() {
        return "pnml";
    }

    @Override
    public void modelFromFile(AbstractModel model) throws IOException, IllegalResourceException, IllegalModelException {
        checkModel(model);
        if (!(model instanceof PnmlModel)) {
            throw new IllegalModelException(String.format("Wrong kind of model (%s), should be %s",
                    model.getClass().toString(),
                    PnmlModel.class.toString()));
        }
        PnmlModel pnmlModel = (PnmlModel) model;
        PnmlImport pnmlImport = new PnmlImport();
        HLAPIClass rawModel = null;
        try {
            rawModel = pnmlImport.importFile(pnmlModel.getResource().getAbsolutePath());
        } catch (BadFileFormatException | UnhandledNetType | ValidationFailedException | InnerBuildException | OCLValidationFailed | OtherException | AssociatedPluginNotFound | InvalidIDException | VoidRepositoryException e) {
            throw new IllegalResourceException(e.getMessage());
        }
        pnmlModel.setDoc((PetriNetDocHLAPI) rawModel);
        if (pnmlModel.getDoc().getNetsHLAPI().size() == 0) {
            throw new IllegalModelException("PNML resource is incorrect (no net in PNML doc)");
        }
        pnmlModel.setModel(pnmlModel.getDoc().getNetsHLAPI().get(0)); // if more than one net, use the first one
    }
}
