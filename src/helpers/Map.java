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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * A class used to perform maps
 * map : X collection, X->Y -> Y collection
 * (for people not using Java 8)
 */
public final class Map {
    // do not instanciate, use static methods
    private Map() {
    }

    /**
     * calls a method on each element of a list and returns the resulting list
     *
     * @param elements a collection of elements of type X
     * @param o        instance which own the method to be called
     * @param m        method to be called, of type X->Y
     * @param <X>
     * @param <Y>
     * @return the list [m(elements[0], m(elements[1]), ...]
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static <X, Y> List<Y> map(List<X> elements, Object o, Method m) throws InvocationTargetException, IllegalAccessException {
        if (elements == null || o == null || m == null) {
            return null;
        }
        try {
            List<Y> rtr = elements.getClass().newInstance();
            for (X element : elements) {
                Y result = (Y) m.invoke(o, element);
                rtr.add(result);
            }
            return rtr;
        } catch (InstantiationException e) {
            return null;
        }
    }
}
