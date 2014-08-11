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
package models.choreography.cif;

import models.base.AbstractModel;
import models.base.AbstractModelReader;
import models.base.IllegalModelException;
import models.base.IllegalResourceException;
import models.choreography.cif.generated.Choreography;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class CifCifReader extends AbstractModelReader {
    @Override
    public String getSuffix() {
        return "cif";
    }

    @Override
    public void modelFromFile(AbstractModel model) throws IOException, IllegalResourceException, IllegalModelException {
        checkModel(model);
        if (!(model instanceof CifModel)) {
            throw new IllegalModelException(String.format("Wrong kind of model (%s), should be %s",
                    model.getClass().toString(),
                    CifModel.class.toString()));
        }
        CifModel cifModel = (CifModel) model;
        // load model using JAXB
        FileInputStream fis;
        try {
            fis = new FileInputStream(cifModel.getResource());
            JAXBContext ctx = JAXBContext.newInstance(Choreography.class);
            Unmarshaller unmarshaller = ctx.createUnmarshaller();
            cifModel.setModel((Choreography) unmarshaller.unmarshal(fis));
            fis.close();
        } catch (FileNotFoundException e) {
            throw new IOException(e.getMessage());
        } catch (JAXBException e) {
            throw new IllegalModelException(e.getMessage());
        }
    }
}
