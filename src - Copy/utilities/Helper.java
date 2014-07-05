/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import twitter4j.HashtagEntity;

/**
 *
 * @author Dimitrios
 */
public class Helper {

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

    public static String[] listToStringArray(List<String> listOfStrings) {
        String[] array = listOfStrings.toArray(new String[listOfStrings.size()]);
        return array;
    }

    public static List<String[]> stringListToStringWordArray(List<String> listOfStrings) {
        List<String[]> wordsArray = new ArrayList<>();
        for (String string : listOfStrings) {
            wordsArray.add(string.split(" "));
        }
        return wordsArray;
    }

    public static boolean isIn(final String string, List<String> collection) {
        return collection.stream().anyMatch((str) -> (str.equals(string)));
    }

    public static String tweetToWords(String status) {
        // Delete all urls
        try {
            String urlPattern = "((https?|ftp|gopher|telnet|file|Unsure|http):((//)|"
                    + "(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
            Pattern p = Pattern.compile(urlPattern, Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(status);

            int i = 0;
            while (m.find()) {
                status = status.replaceAll(m.group(i), " ");
                i++;
            }

            status = deleteStopwords(
                    status
                    .replaceAll("@\\w+", " ")
                    .replaceAll("#\\w+", " ")
                    .replaceAll("\\bdon't\\b", " ")
                    .replaceAll("\n", " ")
                    .replaceAll("[^\\p{L}\\p{N} ]+", " ")
                    .replaceAll("\\b\\w{1,2}\\b\\s?", "")
                    .toLowerCase())
                    .replaceAll(" +", " ")
                    .trim();
        } catch (Exception e) {
            Helper.printErrln("Regex Error: " + e.getMessage());
        }
        return status;
    }
    
    public static String hashtagsToString(HashtagEntity[] hashtags){
        String hashtagString = "";
        
        for (HashtagEntity hashtagEntity : hashtags) {
            hashtagString += hashtagEntity.getText() + " ";
        }
        
        return hashtagString.toLowerCase().replaceAll("[^\\p{ASCII}]", "").trim();
    }

    public static String deleteStopwords(String status) {
        try (BufferedReader br = new BufferedReader(new FileReader("stopwords.txt"))) {
            for (String stopword; (stopword = br.readLine()) != null;) {
                status = status.replaceAll("\\b" + stopword + "\\b", " ");
            }
        } catch (FileNotFoundException ex) {
            printErrln("\"stopwords.txt\" is missing!");
        } catch (IOException io) {
            printErrln(io.getMessage());
        } finally {
            return status;
        }
    }

    public static boolean datesAreEqual(Date first, Date second) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(first).equals(sdf.format(second));
    }
    
    public static void htmlTableBuilder(List<Map.Entry<String, Long>> topEntries){
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
