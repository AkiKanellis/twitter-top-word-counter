package utilities.tweetshandling;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import utilities.generalutils.Printer;

/**
 * Static class with utilities for cleaning a tweet from unimportant text.
 *
 * @author Kanellis Dimitris
 */
public class TweetCleaning {

    /**
     * Cleans up a tweet's full text.
     *
     * Adds a space in place of the URLs from the status, every username, every
     * hashtag, every breakline, every character that is not a letter or a
     * number, every word less than 3 letters and converts everything to
     * lowercase. After that it removes the stopwords and then any extra spaces.
     *
     * @param status the status to clean up
     * @return the edited text of the status
     */
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

    /**
     * For every stopword in the file stopwords.txt it replaces it with a space
     * in the status.
     *
     * @param status the status to remove the stopwords from
     * @return the edited text of the status
     */
    private static String removeStopwords(String status) {
        try (BufferedReader br
                = new BufferedReader(new FileReader("stopwords.txt"))) {

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

    /**
     * For every URL that exists in the status in replaces it with a space in
     * the status.
     *
     * @param status the status to remove the URLs from
     * @return the edited text of the status
     */
    private static String removeURLs(String status) {
        String urlPattern = "((https?|ftp|gopher|telnet|file|Unsure|http):((//)"
                + "|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern p = Pattern.compile(urlPattern, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(status);

        while (m.find()) {
            status = status.replace(m.group(), " ");
        }
        return status;
    }

    /**
     * Makes any double and up space to a single space.
     *
     * @param status the status to edit
     * @return the edited text of the status
     */
    private static String removeExtraSpaces(final String status) {
        return status.replaceAll(" +", " ").trim();
    }
}
