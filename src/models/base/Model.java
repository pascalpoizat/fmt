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

import models.lts.LtsWriter;

import java.io.File;
import java.io.IOException;

/**
 * Created by pascalpoizat on 11/01/2014.
 */
public abstract class Model {

    private File resource;

    public Model() {
        resource = null;
    }

    // sets resource
    public void setResource(File resource) throws IllegalResourceException {
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

    // loads model (just sets the flag, to be overriden + call to super)
    public void load() throws IOException, IllegalResourceException, IllegalModelException {
    }

    // dumps model
    public abstract void dump() throws IOException, IllegalResourceException;

    // writes model to a String
    public String modelToString(ModelWriter writer) throws IllegalResourceException {
        return writer.modelToString(this);
    }

    // writes model to a file
    public void modelToFile(ModelWriter writer) throws IllegalResourceException, IOException {
        writer.modelToFile(this);
    }

    // reads model from a String
    public void modelFromString(ModelReader reader, String string) throws IllegalResourceException {
        reader.modelFromString(this, string);
    }

    // reads model from a file
    public void modelFromFile(ModelReader reader) throws IllegalResourceException, IOException {
        reader.modelFromFile(this);
    }

    // finalization (cleans up resources)
    public void cleanUp() {
        resource = null;
    }

}
