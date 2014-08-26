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
package models.choreography.bpmn;

import models.base.AbstractModel;
import models.base.AbstractModelWriter;
import models.base.IllegalModelException;
import models.base.IllegalResourceException;

import java.io.IOException;

public class BpmnEMFBpmnWriter extends AbstractModelWriter {
    @Override
    public String getSuffix() {
        return "bpmn";
    }

    @Override
    public void modelToFile(AbstractModel model) throws IOException, IllegalResourceException, IllegalModelException {
        checkModel(model, BpmnModel.class);
        throw new RuntimeException();
        // the following is not OK, we should save the WHOLE MODEL, not only the choreography
        /*
        if (!(model instanceof BpmnModel)) {
            throw new IllegalModelException(String.format("Wrong kind of model (%s), should be %s",
                    model.getClass().toString(),
                    BpmnModel.class.toString()));
        }
        BpmnModel bpmnModel = (BpmnModel) model;
        // save the model using the BPMN EMF model
        if (!EMFPlugin.IS_ECLIPSE_RUNNING) {
            Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(getSuffix(), new Bpmn2ResourceFactoryImpl());
        }
        URI uri = URI.createURI(bpmnModel.getResource().getPath());
        Resource res = new ResourceSetImpl().createResource(uri);
        res.getContents().add(bpmnModel.getModel());
        res.save(null);
        */
    }
}
