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
 * Static class including utilities for handling of properties files.
 *
 * @author Kanellis Dimitris
 */
public class PropertiesFilesUtils {

    /**
     * Checks if there is any kind of error in the config.properties file.
     *
     * @return true if error was found, false if not
     */
    public static boolean propertiesErrorFound() {
        File propertiesFile = new File("config.properties");
        return !propertiesFile.exists() || propertiesFileCorrupted(propertiesFile);
    }

    /**
     * Checks if the config.properties file is corrupted by making sure that the
     * properties within it are not null.
     *
     * @return true if corrupted, false if not
     */
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

    /**
     * Creates a new config.properties file and sets the default values to it.
     */
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

    /**
     * Gets the property value from the given key
     *
     * @param key the given key
     * @return the key's value
     */
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

    /**
     * Updates the property file with the Pair that was given.
     *
     * @param pair the Pair of <Key, Value> tha was given
     */
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

    /**
     * Just like updatePropertyFile(final Pair<String, String> pair) except this
     * one updates the file with multiple <Key, Value> Pairs that were given in
     * the form of a list.
     *
     * @param pairs the list of Pairs
     */
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
