/**
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * fmt
 * Copyright (C) 2014 Pascal Poizat (@pascalpoizat)
 * emails: pascal.poizat@lip6.fr
 */

package models.pnml;

import fr.lip6.move.pnml.framework.utils.exception.InvalidIDException;
import fr.lip6.move.pnml.framework.utils.exception.VoidRepositoryException;
import fr.lip6.move.pnml.ptnet.hlapi.PetriNetHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.PetriNetDocHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.PNTypeHLAPI;
import fr.lip6.move.pnml.ptnet.hlapi.NameHLAPI;
import fr.lip6.move.pnml.framework.utils.ModelRepository;
import models.base.AbstractModel;

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
        } catch (InvalidIDException | VoidRepositoryException e) {
        }
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

