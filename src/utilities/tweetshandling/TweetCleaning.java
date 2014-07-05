/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities.tweetshandling;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import utilities.generalutils.Printer;

/**
 *
 * @author Dimitris
 */
public class TweetCleaning {

    public static String tweetToWords(String status) {
        status = removeURLs(status);
        status = status
                .replaceAll("@\\w+", " ")
                .replaceAll("#\\w+", " ")
                .replaceAll("\\bdon't\\b", " ")
                .replaceAll("\n", " ")
                .replaceAll("[^\\p{L}\\p{N} ]+", " ")
                .replaceAll("\\b\\w{1,2}\\b\\s?", "")
                .toLowerCase();
        status = removeStopwords(status);
        status = removeExtraSpaces(status);

        return status;
    }

    private static String removeURLs(String status) {
            String urlPattern = "((https?|ftp|gopher|telnet|file|Unsure|http):((//)|"
                    + "(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
            Pattern p = Pattern.compile(urlPattern, Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(status);

            while (m.find()) {
                status = status.replace(m.group(), " ");
            }
        return status;
    }

    private static String removeExtraSpaces(String status) {
        return status.replaceAll(" +", " ").trim();
    }

    private static String removeStopwords(String status) {
        try (BufferedReader br = new BufferedReader(new FileReader("stopwords.txt"))) {
            for (String stopword; (stopword = br.readLine()) != null;) {
                status = status.replaceAll("\\b" + stopword + "\\b", " ");
            }
        } catch (FileNotFoundException ex) {
            Printer.printErrln("\"stopwords.txt\" is missing!");
        } catch (IOException io) {
            Printer.printErrln(io.getMessage());
        } finally {
            return status;
        }
    }
}
