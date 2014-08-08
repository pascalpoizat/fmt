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

import models.base.FmtException;
import transformations.base.ITransformer;
import models.choreography.bpmn.BpmnFactory;
import models.choreography.cif.CifFactory;

import java.io.IOException;

/**
 * Created by pascalpoizat on 10/01/2014.
 */
public class Bpmn2Cif {
    public static void main(String[] args) {
        BpmnFactory bpmnFactory = BpmnFactory.getInstance();
        CifFactory cifFactory = CifFactory.getInstance();
        ITransformer trans = new Bpmn2CifTransformer(bpmnFactory, cifFactory);
        trans.setVerbose(true);
        trans.about();
        if (args.length == 0) {
            trans.error("missing file argument");
            return;
        }
        try {
            trans.setResources(args[0]);
            trans.load();
            trans.transform();
            trans.dump();
            trans.cleanUp();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (FmtException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (Throwable e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

}
