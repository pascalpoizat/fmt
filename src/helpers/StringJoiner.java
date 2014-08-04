package helpers;

import java.util.Iterator;
import java.util.List;

/**
 * Created by pascalpoizat on 04/08/2014.
 */

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
