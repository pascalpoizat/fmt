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

package transformations.base;

import models.base.*;

import java.io.IOException;

/**
 * Created by Pascal Poizat (@pascalpoizat) on 11/01/2014.
 */
public interface Transformer {

    // sets the models to work with
    void setResources(AbstractModel inputModel, AbstractModel outputModel, AbstractModelReader reader, AbstractModelWriter writer) throws IllegalResourceException;

    // perform the transformation, if lazy perform the transformation only if needed
    // i.e., if the output model does not exist or is more recent than the input model
    void run(boolean lazy) throws IOException, IllegalResourceException, IllegalModelException;

    // load input model
    void load() throws IOException, IllegalResourceException, IllegalModelException;

    // perform transformation between input model and output model
    void transform() throws IllegalResourceException, IllegalModelException;

    // dump output model
    void dump() throws IOException, IllegalResourceException, IllegalModelException;

    // finalize (cleans up models)
    void cleanUp();

    // set the verbose mode
    void setVerbose(boolean mode);

    // write a message
    void message(String msg);

    // write an error
    void error(String msg);

    // write a warning
    void warning(String msg);

    // get information about the transformation
    void about();
}
