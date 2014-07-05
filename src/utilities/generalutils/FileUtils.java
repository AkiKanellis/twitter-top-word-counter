/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities.generalutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Dimitris
 */
public class FileUtils {

    private static final int WORD_COUNT = 354;

    public static boolean stopwordsCorrupted(File file) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        try (InputStream input = new FileInputStream(file)) {
            DigestInputStream dinput = new DigestInputStream(input, md);
        }
        byte[] digest = md.digest();
        
        
        return true;
    }

    //public static boolean twitterconfCorrupted(File propertiesFile) {
    //}
}
