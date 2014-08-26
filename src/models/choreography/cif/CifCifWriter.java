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

package models.choreography.cif;

import models.base.AbstractModel;
import models.base.AbstractModelWriter;
import models.base.IllegalModelException;
import models.base.IllegalResourceException;
import models.choreography.cif.generated.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.FileOutputStream;
import java.io.IOException;

public class CifCifWriter extends AbstractModelWriter {
    @Override
    public String getSuffix() {
        return "cif";
    }

    @Override
    public void modelToFile(AbstractModel model) throws IOException, IllegalResourceException, IllegalModelException {
        checkModel(model, CifModel.class);
        CifModel cifModel = (CifModel) model;
        try (FileOutputStream fos = new FileOutputStream(cifModel.getResource())) {
            final JAXBContext ctx = JAXBContext.newInstance(Choreography.class);
            final Marshaller marshaller = ctx.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(cifModel.getModel(), fos);
        } catch (JAXBException e) {
            throw new IllegalModelException(e.getMessage());
        }

    }
}
