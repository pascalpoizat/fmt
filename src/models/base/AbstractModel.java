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
 * Created by pascalpoizat on 11/01/2014.
 */
public abstract class AbstractModel {

    private File resource;

    public AbstractModel() {
        resource = null;
    }

    // sets resource
    public void setResource(final File resource) throws IllegalResourceException {
        if (resource == null) {
            throw new IllegalResourceException("No resource given");
        }
        this.resource = resource;
    }

    // gets resource
    public File getResource() {
        return resource;
    }

    // gets regular file suffix for resource
    public abstract String getSuffix();

    // loads model
    public abstract void load() throws IOException, IllegalResourceException, IllegalModelException;

    // dumps model
    public abstract void dump() throws IOException, IllegalResourceException;

    // writes model to a file
    public final void modelToFile(final AbstractModelWriter writer) throws IllegalResourceException, IOException {
        writer.modelToFile(this);
    }

    // reads model from a file
    public final void modelFromFile(final AbstractModelReader reader) throws IllegalResourceException, IOException {
        reader.modelFromFile(this);
    }

    // writes model to a string
    public final String modelToString(final AbstractStringModelWriter writer) throws IllegalResourceException {
        return writer.modelToString(this);
    }

    // finalization (cleans up resources)
    public void cleanUp() {
        resource = null;
    }

}
