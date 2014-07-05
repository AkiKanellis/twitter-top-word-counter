/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities.tweetshandling;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WordCounting {

    public static String getHtmlTable(final List<String[]> words, final List<String[]> hashtags) {
        String htmlTable = "<html>"
                + "<body>";
        List<Map.Entry<String, Long>> topWords = getTopWords(_topX,
                words.stream().flatMap(Arrays::stream))
                .collect(Collectors.toList());
        List<Map.Entry<String, Long>> topHashtags = getTopWords(_topX,
                hashtags.stream().flatMap(Arrays::stream))
                .collect(Collectors.toList());

        htmlTable += "<table>"
                + "<tr>"
                + "<td>" + listToHtmlTable(topWords, "Words used most") + "</td>"
                + "<td>" + listToHtmlTable(topHashtags, "Hashtags used most") + "</td>"
                + "</tr>"
                + "</table>\n"
                + "&nbsp;"
                + "</body>"
                + "</html>";
        return htmlTable;
    }

    public static Stream<Map.Entry<String, Long>> getTopWords(final int topX, final Stream<String> words) {
        if (topX < 1) {
            throw new IllegalArgumentException("Invalid value for topX: " + topX);
        }
        Objects.requireNonNull(words);
        Comparator<Map.Entry<String, Long>> comparator = Comparator.comparingLong(Map.Entry::getValue);
        return words
                .filter(str -> !str.isEmpty())
                .collect(Collectors.groupingBy(i -> i, Collectors.counting()))
                .entrySet().stream()
                .sorted(comparator.reversed())
                .limit(topX);
    }

    public static String listToHtmlTable(List<Map.Entry<String, Long>> topEntries, final String title) {
        String htmlTable = "<table style=\"border:1px dashed black;\">"
                + "<th colspan=\"3\">" + title + "</th>";
        int counter = 1;
        for (Map.Entry<String, Long> entry : topEntries) {
            htmlTable += "<tr>"
                    + "<td>" + counter++ + "</td>"
                    + "<td>" + entry.getKey() + "</td>"
                    + "<td>" + entry.getValue() + "</td>"
                    + "</tr>";
        }
        htmlTable += "</table>";

        return htmlTable;
    }

    private static final int _topX = 10;
    public static final String DASHES = new String(new char[80]).replace("\0", "-");
}
