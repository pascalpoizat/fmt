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

package models.lts;

import models.base.IllegalResourceException;
import models.base.Model;
import models.base.ModelReader;

/**
 * Created by pascalpoizat on 06/08/2014.
 */
public abstract class LtsReader extends ModelReader {
    // from ModelReader

    // returns the suffix of the files the writer works with
    @Override
    public abstract String getSuffix();

    // reads model from a String
    @Override
    public abstract void modelFromString(Model model, String string) throws IllegalResourceException;

    // specific to LTS

}
