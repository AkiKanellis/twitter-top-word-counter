/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities.generalutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author Kanellis Dimitris
 */
public class PropertiesFilesUtils {

    public static boolean propertiesErrorFound() {
        File propertiesFile = new File("config.properties");
        return !propertiesFile.exists() || propertiesFileCorrupted(propertiesFile);
    }

    private static boolean propertiesFileCorrupted(File propertiesFile) {
        boolean corrupted = false;

        try (InputStream input = new FileInputStream(propertiesFile);) {
            Properties prop = new Properties();
            List<String> properties = Arrays.asList("username", "password",
                    "hostname", "port");
            prop.load(input);

            for (String property : properties) {
                if (prop.getProperty(property) == null) {
                    corrupted = true;
                    break;
                }
            }
            return corrupted;
        } catch (IOException ex) {
            Printer.printErrln("IO Error: " + ex.getMessage());
            return corrupted;
        }
    }

    public static void setDefaultPropertyFile() {
        try (OutputStream output = new FileOutputStream("config.properties");) {
            Properties prop = new Properties();
            prop.setProperty("username", "root");
            prop.setProperty("password", "");
            prop.setProperty("hostname", "localhost");
            prop.setProperty("port", "3306");

            prop.store(output, null);
        } catch (IOException io) {
            System.out.println("IO Error: " + io.getMessage());
        }
    }

    public static String getPropertyValue(final String key) {
        String value = null;

        try (InputStream input = new FileInputStream("config.properties");) {
            Properties prop = new Properties();
            prop.load(input);
            
            value = prop.getProperty(key);
            return value;
        } catch (IOException ex) {
            Printer.printErrln("IO Error: " + ex.getMessage());
            return value;
        }
    }

    public static void updatePropertyFile(final Pair<String, String> pair) {
        Properties prop = new Properties();
        OutputStream output = null;
        InputStream input = null;

        try {
            input = new FileInputStream("config.properties");
            prop.load(input);

            prop.setProperty(pair.left, pair.right);

            output = new FileOutputStream("config.properties");
            prop.store(output, null);

        } catch (IOException io) {
            System.out.println("IO Error: " + io.getMessage());
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
            } catch (IOException io) {
                System.out.println("IO Error: " + io.getMessage());
            }
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException io) {
                System.out.println("IO Error: " + io.getMessage());
            }
        }
    }

    public static void updatePropertyFile(final List<Pair<String, String>> pairs) {
        Properties prop = new Properties();
        OutputStream output = null;
        InputStream input = null;

        try {
            input = new FileInputStream("config.properties");
            prop.load(input);

            pairs.stream().forEach((pair) -> {
                prop.setProperty(pair.left, pair.right);
            });

            output = new FileOutputStream("config.properties");
            prop.store(output, null);

        } catch (IOException io) {
            System.out.println("IO Error: " + io.getMessage());
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
            } catch (IOException io) {
                System.out.println("IO Error: " + io.getMessage());
            }
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException io) {
                System.out.println("IO Error: " + io.getMessage());
            }
        }
    }
}
