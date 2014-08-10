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

import models.base.AbstractModel;
import models.base.IllegalResourceException;
import models.choreography.stg.*;
import java.io.File;
import java.io.IOException;

public class CopyPaste {
    public static void main(String[] args) {
        try {
            // read the input model using an stg/STG reader
            AbstractModel model = new StgModel();
            model.setResource(new File("sample.stg"));
            model.modelFromFile(new StgStgReader());

            // write the model using an STG/stg writer
            model.setResource(new File("sample_copy.stg"));
            model.modelToFile(new StgStgWriter());

            // write the model using an STG/dot writer
            model.setResource(new File("sample_copy.dot"));
            model.modelToFile(new DotStgWriter());

        } catch (IllegalResourceException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
