/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities.generalutils;

import java.util.ArrayList;
import java.util.List;
import twitter4j.HashtagEntity;

/**
 *
 * @author Dimitris
 */
public class Converter {

    public static String[] listToStringArray(List<String> listOfStrings) {
        String[] array = listOfStrings.toArray(new String[listOfStrings.size()]);
        return array;
    }

    public static List<String[]> stringListToStringWordArray(List<String> listOfStrings) {
        List<String[]> wordsArray = new ArrayList<>();
        listOfStrings.stream().forEach((string) -> {
            wordsArray.add(string.split(" "));
        });
        return wordsArray;
    }
    
    public static String hashtagArrayToString(HashtagEntity[] hashtags) {
        StringBuilder hashtagString = new StringBuilder();

        for (HashtagEntity hashtagEntity : hashtags) {
            hashtagString.append(hashtagEntity.getText()).append(" ");
        }
        return hashtagString.toString().toLowerCase().replaceAll("[^\\p{ASCII}]", "").trim();
    }
}
