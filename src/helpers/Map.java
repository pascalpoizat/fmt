package helpers;

/**
 * Created by pascalpoizat on 04/08/2014.
 */

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
