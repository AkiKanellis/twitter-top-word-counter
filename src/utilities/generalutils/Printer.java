/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities.generalutils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Kanellis Dimitris
 */
public class Printer {

    public static void print(String message) {
        message = message.replaceAll("\n+", ". ");
        System.out.print(getFormattedTime() + " " + message);
    }

    public static void println(String message) {
        message = message.replaceAll("\n+", ". ");
        System.out.println(getFormattedTime() + " " + message);
    }

    public static void printErrln(String message) {
        message = message.replaceAll("\n+", ". ");
        System.err.println(getFormattedTime() + " " + message);
    }

    private static String getFormattedTime() {
        return "[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "]";
    }
}
