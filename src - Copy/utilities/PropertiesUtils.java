/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

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
 * @author Dimitrios
 */
public class PropertiesUtils {

    public static boolean propertiesErrorFound() {
        File propertiesFile = new File("config.properties");

        return !propertiesFile.exists() || propertiesCorrupted(propertiesFile);
    }

    private static boolean propertiesCorrupted(File propertiesFile) {
        Properties prop = new Properties();
        InputStream input = null;
        boolean corrupted = false;
        List<String> properties = Arrays.asList("username", "password", "hostname",
                "port");

        try {
            input = new FileInputStream(propertiesFile);

            prop.load(input);

            for (String property : properties) {
                if (prop.getProperty(property) == null) {
                    corrupted = true;
                    break;
                }
            }

        } catch (IOException ex) {
            Helper.printErrln("IO Error: " + ex.getMessage());
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
            return corrupted;
        }
    }

    public static void setDefaultPropertyFile() {
        Properties prop = new Properties();
        OutputStream output = null;
        InputStream input = null;

        try {
            prop.setProperty("username", "root");
            prop.setProperty("password", "");
            prop.setProperty("hostname", "localhost");
            prop.setProperty("port", "3306");

            output = new FileOutputStream("config.properties");
            prop.store(output, null);

        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public static String getPropertyValue(final String key) {
        Properties prop = new Properties();
        InputStream input = null;
        String value = null;

        try {

            input = new FileInputStream("config.properties");

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            value = prop.getProperty(key);

        } catch (IOException ex) {
            Helper.printErrln("IO Error: " + ex.getMessage());
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
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
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

            for (Pair<String, String> pair : pairs) {
                prop.setProperty(pair.left, pair.right);
            }

            output = new FileOutputStream("config.properties");
            prop.store(output, null);

        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
