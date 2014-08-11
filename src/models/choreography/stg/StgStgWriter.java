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
package models.choreography.stg;

import models.base.AbstractModel;
import models.base.IllegalResourceException;

public class StgStgWriter extends AbstractStgWriter {
    @Override
    public String getSuffix() {
        return "stg";
    }

    @Override
    public String modelToString(AbstractModel model) throws IllegalResourceException {
        if (!(model instanceof StgModel)) {
            throw new IllegalResourceException(String.format("Wrong kind of model (%s), should be %s",
                    model.getClass().toString(),
                    StgModel.class.toString()));
        }
        StgModel stgModel = (StgModel) model;
        return stgModel.toStg();
    }
}
