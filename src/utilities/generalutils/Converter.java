/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities.generalutils;

import java.util.ArrayList;
import java.util.List;
import twitter4j.GeoLocation;
import twitter4j.HashtagEntity;

/**
 *
 * @author Kanellis Dimitris
 */
public class Converter {

    public static String[] listToStringArray(final List<String> listOfStrings) {
        return listOfStrings.toArray(new String[listOfStrings.size()]);
    }

    public static List<String[]> stringListToStringWordArray(
            final List<String> listOfStrings) {
        List<String[]> wordsArray = new ArrayList<>();
        listOfStrings.stream().forEach((string) -> {
            wordsArray.add(string.split(" "));
        });
        return wordsArray;
    }

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
