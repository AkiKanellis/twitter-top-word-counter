/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities.generalutils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Dimitrios
 */
public class Printer {

    public static String getTime() {
        return '[' + new SimpleDateFormat("HH:mm:ss").format(new Date()) + ']';
    }

    public static void print(String message) {
        message = message.replaceAll("\n+", ". ");
        System.out.print(getTime() + ' ' + message);
    }

    public static void println(String message) {
        message = message.replaceAll("\n+", ". ");
        System.out.println(getTime() + ' ' + message);
    }

    public static void printErrln(String message) {
        message = message.replaceAll("\n+", ". ");
        System.err.println(getTime() + ' ' + message);
    }

    // FIXME move these to a separate class
    public static boolean isIn(final String string, List<String> collection) {
        return collection.stream().anyMatch((str) -> (str.equals(string)));
    }

    

    public static boolean datesAreEqual(Date first, Date second) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(first).equals(sdf.format(second));
    }

    public static void htmlTableBuilder(List<Map.Entry<String, Long>> topEntries) {
        String html = "<table>";
        int counter = 1;
        for (Map.Entry<String, Long> entry : topEntries) {
            html += "\n<tr>"
                    + "\n<td>" + counter++ + "</td>"
                    + "\n<td>" + ": " + "</td>"
                    + "\n<td>" + entry.getKey() + "</td>"
                    + "\n<td>" + " - " + "</td>"
                    + "\n<td>" + entry.getValue() + "</td>"
                    + "\n</tr>";
        }
        html += "\n</table>";
    }
}
