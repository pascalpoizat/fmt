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

package transformations.bpmn2cif;

import models.base.*;
import models.choreography.bpmn.BpmnEMFBpmnReader;
import models.choreography.bpmn.BpmnFactory;
import models.choreography.bpmn.BpmnModel;
import models.choreography.cif.CifCifWriter;
import models.choreography.cif.CifModel;
import transformations.base.Transformer;

import java.io.IOException;

public class Bpmn2Cif {

    public static final String USAGE = "Bpmn2Cif input_file output_file";

    public static void main(String[] args) {
        Transformer trans = new Bpmn2CifTransformer();
        trans.setVerbose(true);
        trans.about();
        if (args.length != 2) {
            trans.error(USAGE);
            return;
        }
        try {
            AbstractModelReader reader = new BpmnEMFBpmnReader();
            AbstractModelWriter writer = new CifCifWriter();
            AbstractModel input_model = new BpmnModel();
            AbstractModel output_model = new CifModel();
            trans.setResources(input_model, output_model, reader, writer);
            trans.load();
            trans.transform();
            trans.dump();
            trans.cleanUp();
        } catch (IOException | IllegalResourceException | IllegalModelException e) {
            trans.message(e.getMessage());
            e.printStackTrace();
        }
    }

}
