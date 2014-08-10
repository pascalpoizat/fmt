package models.stg;

import models.base.IllegalModelException;
import models.base.IllegalResourceException;
import models.base.Model;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;

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

public class StgModel extends Model {

    public StgModel() {
        super();
    }

    @Override
    public String getSuffix() {
        return "stg";
    }

    @Override
    public void load() throws IOException, IllegalResourceException, IllegalModelException {
        throw new NotImplementedException();
    }

    @Override
    public void dump() throws IOException, IllegalResourceException {

    }

    @Override
    public void cleanUp() {
        super.cleanUp();
    }
}
