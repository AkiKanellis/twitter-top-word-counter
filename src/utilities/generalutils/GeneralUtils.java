package utilities.generalutils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Static class including general utilities of various use.
 *
 * @author Kanellis Dimitris
 */
public class GeneralUtils {

    /**
     * Checks to see if a given String is in the collection given.
     *
     * @param string the String to check
     * @param collection the collection that is given
     * @return true if the String is included, false if not
     */
    public static boolean isIn(final String string, List<String> collection) {
        return collection.stream().anyMatch((str) -> (str.equals(string)));
    }

    /**
     * Checks if two dates are equal by converting them in SimpleDateFormat and
     * then comparing them.
     *
     * @param first the first Date to check
     * @param second the second Date to check
     * @return true if they are equal, false if not
     */
    public static boolean datesAreEqual(Date first, Date second) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(first).equals(sdf.format(second));
    }
}
