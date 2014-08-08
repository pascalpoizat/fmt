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
 * Created by pascalpoizat on 06/08/2014.
 */
public abstract class ModelReader {
    // returns the suffix of the files the writer works with
    public abstract String getSuffix();

    // reads model from a file
    // by default, uses modelFromString(), can be modified by inheritance (eg for readers using EMF or XML)
    public void modelFromFile(Model model) throws IOException, IllegalResourceException {
        if (model == null || model.getResource() == null) {
            throw new IllegalResourceException("");
        }
        if (!model.getResource().getName().endsWith("." + getSuffix())) {
            throw new IllegalResourceException("Wrong file suffix (should be " + getSuffix() + ")");
        }
        FileReader file = new FileReader(model.getResource());
        BufferedReader bufferedReader = new BufferedReader(file);
        String s;
        StringBuilder builder = new StringBuilder();
        while ((s = bufferedReader.readLine()) != null) {
            builder.append(s+"\n");
        }
        file.close();
        modelFromString(model, builder.toString());
    }

    // reads model from a String
    public abstract void modelFromString(Model model, String string) throws IllegalResourceException;
}
