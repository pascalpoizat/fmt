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

package models.lts;

import models.base.AbstractModel;
import models.base.AbstractModelFactory;

public final class LtsFactory extends AbstractModelFactory {

    private static LtsFactory instance;

    private LtsFactory() {
        super();
    }

    public static LtsFactory getInstance() {
        if (instance == null) {
            instance = new LtsFactory();
        }
        return instance;
    }

    public AbstractModel create() {
        return new LtsModel();
    }

}
