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

package models.base;

import java.io.File;
import java.io.IOException;

/**
 * Created by pascalpoizat on 05/02/2014.
 */
public abstract class ModelFactory implements AbstractModelFactory {
    @Override
    public abstract Model create();

    @Override
    public Model createFromFile(String filename) throws IOException, IllegalResourceException, IllegalModelException {
        Model model = create();
        model.setResource(new File(filename));
        model.load();
        return model;
    }
}
