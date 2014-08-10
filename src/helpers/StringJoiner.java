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

package helpers;

import java.util.Iterator;
import java.util.List;

/**
 * A class used to join Strings
 * (for people not using Java 8)
 */

public final class StringJoiner {
    // do not instanciate, use static methods
    private StringJoiner() {
    }

    /**
     * joins the elements of a list of string using a separator
     * @param separator the string to separate the elements of the list
     * @param elements a list of strings
     * @return a string made up of all elements separated by separator
     */
    public static String join(String separator, List<String> elements) {
        if (separator==null || elements==null) {
            return null;
        }
        String rtr = "";
        Iterator it = elements.iterator();
        if (!it.hasNext()) {
            return rtr;
        }
        rtr = (String) it.next();
        while (it.hasNext()) {
            rtr = rtr + separator + it.next();
        }
        return rtr;
    }
}
