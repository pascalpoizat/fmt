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

import java.io.*;

/**
 * this is the abstract class for model readers that use modelFromString to achieve modelFromFile
 */
public abstract class AbstractStringModelReader extends AbstractModelReader {

    // reads model from a file
    @Override
    public final void modelFromFile(final AbstractModel model) throws IOException, IllegalResourceException {
        if (model == null || model.getResource() == null) {
            throw new IllegalResourceException("");
        }
        if (!model.getResource().getName().endsWith("." + getSuffix())) {
            throw new IllegalResourceException("Wrong file suffix (should be " + getSuffix() + ")");
        }
        final FileReader file = new FileReader(model.getResource());
        final BufferedReader bufferedReader = new BufferedReader(file);
        final StringBuilder builder = new StringBuilder();
        String nextLine;
        nextLine = bufferedReader.readLine();
        while (nextLine != null) {
            builder.append(nextLine).append('\n');
            nextLine = bufferedReader.readLine();
        }
        file.close();
        modelFromString(model, builder.toString());
    }

    // reads model from a String
    public abstract void modelFromString(AbstractModel model, String stringModel) throws IllegalResourceException;
}
