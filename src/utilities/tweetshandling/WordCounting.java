package utilities.tweetshandling;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class for managing the calculation of the most used words and hastags.
 *
 * @author Kanellis Dimitris
 */
public class WordCounting {

    /**
     * Returns an HTML which contains the two HTML tables of the top words and
     * hashtags by firstly getting each list of maps and then converting them to
     * HTML tables.
     *
     * @param words the list of String arrays which are the words that were used
     * @param hashtags the list of String arrays which are the hashtags that
     * were used
     * @return the properly formatted HTML table
     */
    public static String getHtmlTable(final List<String[]> words,
            final List<String[]> hashtags) {

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
                + "<td>"
                + listToHtmlTable(topWords, "Words used most")
                + "</td>"
                + "<td>"
                + listToHtmlTable(topHashtags, "Hashtags used most")
                + "</td>"
                + "</tr>"
                + "</table>\n"
                + "&nbsp;"
                + "</body>"
                + "</html>";
        return htmlTable;
    }

    /**
     * Gets the most used words/hashtags from a stream of Strings of
     * words/hashtags by grouping them by word/hashtag and have the total
     * occurrences by counting the groupings and they also get sorted in
     * descending order.
     *
     * @param topX the number of top words to keep
     * @param words the Stream of Strings to count from
     */
    private static Stream<Map.Entry<String, Long>> getTopWords(final int topX,
            final Stream<String> words) {

        if (topX < 1) {
            throw new IllegalArgumentException("Invalid value for topX: "
                    + topX);
        }
        Objects.requireNonNull(words);
        Comparator<Map.Entry<String, Long>> comparator
                = Comparator.comparingLong(Map.Entry::getValue);

        return words
                .filter(str -> !str.isEmpty())
                .collect(Collectors.groupingBy(i -> i, Collectors.counting()))
                .entrySet().stream()
                .sorted(comparator.reversed())
                .limit(topX);
    }

    /**
     * Converts a list of Maps to an HTML table with the title that is given.
     *
     * @param topEntries the topX entries of the words/hashtags
     * @param title the tile of the HTML table
     */
    private static String listToHtmlTable(
            List<Map.Entry<String, Long>> topEntries, final String title) {

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
}
