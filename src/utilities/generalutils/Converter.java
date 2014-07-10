package utilities.generalutils;

import java.util.ArrayList;
import java.util.List;
import twitter4j.GeoLocation;
import twitter4j.HashtagEntity;

/**
 * Static class for utilities regarding conversion from one type to another.
 *
 * @author Kanellis Dimitris
 */
public class Converter {

    /**
     * Converts a list of Strings to a String array.
     *
     * @param listOfStrings the list to convert
     * @return the String array
     */
    public static String[] listToStringArray(final List<String> listOfStrings) {
        return listOfStrings.toArray(new String[listOfStrings.size()]);
    }

    /**
     * Converts a list of Strings to a list of String arrays where every element
     * in the array is a word from the String.
     *
     * @param listOfStrings the list to convert
     * @return a list of String arrays where every element in the array is a
     * word from the String
     */
    public static List<String[]> stringListToStringWordArray(
            final List<String> listOfStrings) {
        List<String[]> wordsArray = new ArrayList<>();
        listOfStrings.stream().forEach((string) -> {
            wordsArray.add(string.split(" "));
        });
        return wordsArray;
    }

    /**
     * Converts an array of hashtags to a unified String of all those hashtags.
     *
     * @param hashtags the hashtag array to convert
     * @return the String which contains all the hashtags separated by space
     */
    public static String hashtagArrayToString(final HashtagEntity[] hashtags) {
        StringBuilder stringOfHashtags = new StringBuilder();

        for (HashtagEntity hashtagEntity : hashtags) {
            stringOfHashtags.append(hashtagEntity.getText()).append(" ");
        }
        return stringOfHashtags
                .toString()
                .toLowerCase()
                .replaceAll("[^\\p{ASCII}]", "")
                .trim();
    }

    /**
     * Converts a GeoLocation object to a String of Latitude and Longitude
     * separated by a semicolon.
     *
     * @param geolocation the GeoLocation object to convert
     * @return the String of Latitude and Longitude separated by a semicolon.
     */
    public static String geolocationToString(final GeoLocation geolocation) {
        if (geolocation == null) {
            return null;
        } else {
            return Double.toString(
                    geolocation.getLatitude())
                    + ";"
                    + Double.toString(geolocation.getLongitude());
        }
    }
}
