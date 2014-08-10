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

import models.base.AbstractModel;
import models.base.AbstractModelWriter;
import models.base.IllegalResourceException;

/**
 * Created by pascalpoizat on 04/08/2014.
 */
public abstract class LtsWriter extends AbstractModelWriter {
    // from ModelWriter

    // returns the suffix of the files the writer works with
    @Override
    public abstract String getSuffix();

    // writes model to a String
    @Override
    public abstract String modelToString(AbstractModel model) throws IllegalResourceException;

    // specific to LTS

    // writes a state to a String
    abstract String modelToString(LtsState ltsState) throws IllegalResourceException;

    // writes a transition to a String
    abstract String modelToString(LtsTransition ltsTransition) throws IllegalResourceException;

    // writes a label to a String
    abstract String modelToString(LtsLabel ltsLabel);
}
