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

package transformations.base;

import models.base.IllegalModelException;
import models.base.IllegalResourceException;

import java.io.IOException;

/**
 * Created by pascalpoizat on 11/01/2014.
 */
public interface ITransformer {
    // sets the resources to operate on
    public void setResources(String path_to_input_resource) throws IllegalResourceException;

    // load input model
    public void load() throws IOException, IllegalResourceException, IllegalModelException;

    // perform transformation between input model and output model
    public void transform() throws IllegalModelException;

    // dump output model
    public void dump() throws IOException, IllegalResourceException;

    // finalize (cleans up models)
    public void cleanUp();

    // set the verbose mode
    public void setVerbose(boolean mode);

    // write a message
    public void message(String msg);

    // write an error
    public void error(String msg);

    // write a warning
    public void warning(String msg);

    // get information about the transformation
    public void about();
}
