package utilities.generalutils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Static class including printing utilities.
 *
 * @author Kanellis Dimitris
 */
public class Printer {

    /**
     * Removes any extra newlines from the message and prints it with the
     * correct timestamp.
     *
     * @param message the message to print
     */
    public static void print(String message) {
        message = message.replaceAll("\n+", ". ");
        System.out.print(getFormattedTime() + " " + message);
    }

    /**
     * Removes any extra newlines from the message and prints it with a newline
     * break, with the correct timestamp.
     *
     * @param message the message to print
     */
    public static void println(String message) {
        message = message.replaceAll("\n+", ". ");
        System.out.println(getFormattedTime() + " " + message);
    }

    /**
     * Removes any extra newlines from the error message and prints it message
     * with a newline break, with the correct timestamp.
     *
     * @param message the message to print
     */
    public static void printErrln(String message) {
        message = message.replaceAll("\n+", ". ");
        System.err.println(getFormattedTime() + " " + message);
    }

    /**
     *
     * @return the current time in a properly formatted manner
     */
    private static String getFormattedTime() {
        return "[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "]";
    }
}
