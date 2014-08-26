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
 *   Copyright (C) 2014 Pascal Poizat (@pascalpoizat)
 *   emails: pascal.poizat@lip6.fr
 */

package models.pnml;

import fr.lip6.move.pnml.framework.utils.ModelRepository;
import models.base.AbstractModel;
import models.base.AbstractModelWriter;
import models.base.IllegalModelException;
import models.base.IllegalResourceException;

import java.io.FileWriter;
import java.io.IOException;

public class PnmlPnmlWriter extends AbstractModelWriter {
    @Override
    public String getSuffix() {
        return "pnml";
    }

    @Override
    public void modelToFile(AbstractModel model) throws IOException, IllegalResourceException, IllegalModelException {
        checkModel(model, PnmlModel.class);
        PnmlModel pnmlModel = (PnmlModel) model;
        ModelRepository mr = ModelRepository.getInstance();
        mr.setPrettyPrintStatus(true);
        FileWriter fw = new FileWriter(pnmlModel.getResource().getAbsolutePath());
        fw.write(pnmlModel.getDoc().toPNML());
        fw.close();
    }
}
