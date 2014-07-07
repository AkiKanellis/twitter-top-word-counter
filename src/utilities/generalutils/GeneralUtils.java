/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities.generalutils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Kanellis Dimitris
 */
public class GeneralUtils {

    public static boolean isIn(final String string, List<String> collection) {
        return collection.stream().anyMatch((str) -> (str.equals(string)));
    }

    public static boolean datesAreEqual(Date first, Date second) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(first).equals(sdf.format(second));
    }
}
