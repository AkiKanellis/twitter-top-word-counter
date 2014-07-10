package utilities.sqlhandling;

import utilities.generalutils.Printer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * This class holds information regarding the connection to the server and
 * functions that modify the server.
 *
 * @author Kanellis Dimitris
 */
public class Connector {

    /**
     * The constructor initialises the variables with the given values and the
     * URL to the default one.
     *
     * @param user the user which contains the username and password
     * @param host the hostname
     * @param port the port that will be used
     */
    public Connector(final User user, final String host, final int port) {
        _user = user;
        _host = host;
        _port = port;
        _url = "jdbc:mysql://" + _host + ":" + _port + "/";
    }

    /**
     *
     * @return the user of the Connector object
     */
    public User getUser() {
        return _user;
    }

    /**
     *
     * @return the URL of the Connector object
     */
    public String getURL() {
        return _url;
    }

    /**
     *
     * @return the driver of the Connector object
     */
    public String getDriver() {
        return DRIVER;
    }

    /**
     * Checks the connection with the server by the requesting it's version and
     * then returns it.
     *
     * @return the version of the server if connection was established, or an
     * empty String if it a connection could not be made
     */
    public String checkConnection() {
        Printer.println(GETTING_DRIVER_MESSAGE);
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            Printer.printErrln("Driver Error: " + e.getMessage());
            return VERSION_NOT_FOUND;
        }

        Printer.println(CONNECTING_MESSAGE);
        try (Connection con = DriverManager.getConnection(_url,
                _user.getUsername(),
                _user.getPassword());
                Statement stmt = con.createStatement();) {

            Printer.println("Getting server version...");
            try (ResultSet rs = stmt.executeQuery(VERSION_QUERY);) {
                rs.next();
                return rs.getString(1);
            }
        } catch (SQLException se) {
            Printer.printErrln("SQL Error: " + se.getErrorCode() + " "
                    + se.getMessage());
            return VERSION_NOT_FOUND;
        }
    }

    /**
     * Returns a the list of databases that the server contains through the use
     * of Metadata.
     *
     * @return a list of databases names
     */
    public List<String> getDatabases() {
        List<String> databases = new ArrayList<>();

        Printer.println(GETTING_DRIVER_MESSAGE);
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            Printer.printErrln("Driver Error: " + e.getMessage());
            return databases;
        }

        Printer.println(CONNECTING_MESSAGE);
        try (Connection con = DriverManager.getConnection(_url,
                _user.getUsername(),
                _user.getPassword());) {

            Printer.println("Retrieving databases...");
            try (ResultSet rs = con.getMetaData().getCatalogs();) {
                while (rs.next()) {
                    databases.add(rs.getString("TABLE_CAT"));
                }
                return databases;
            }
        } catch (SQLException se) {
            Printer.printErrln("SQL Error: " + se.getErrorCode() + " "
                    + se.getMessage());
            return databases;
        }
    }

    /**
     * Creates a new database in the server with the name that was given.
     *
     * @param databaseName the name of the database to create
     */
    public void createDatabase(final String databaseName) {
        Printer.println(GETTING_DRIVER_MESSAGE);
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            Printer.printErrln("Driver Error: " + e.getMessage());
        }

        Printer.println(CONNECTING_MESSAGE);
        try (Connection con = DriverManager.getConnection(_url,
                _user.getUsername(),
                _user.getPassword());
                Statement stmt = con.createStatement();) {
            Printer.println("Creating Database...");
            stmt.executeUpdate(CREATE_DATABASE_QUERY + databaseName);
        } catch (SQLException se) {
            Printer.printErrln("SQL Error " + se.getErrorCode() + " "
                    + se.getMessage());
        }
    }

    /**
     * Deletes a database from the server with the name that was given.
     *
     * @param databaseName the database to delete
     */
    public void deleteDatabase(final String databaseName) {
        Printer.println(GETTING_DRIVER_MESSAGE);
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            Printer.printErrln("Driver Error: " + e.getMessage());
        }

        Printer.println(CONNECTING_MESSAGE);
        try (Connection con = DriverManager.getConnection(_url,
                _user.getUsername(),
                _user.getPassword());
                Statement stmt = con.createStatement();) {

            Printer.println("Deleting Database...");
            stmt.executeUpdate(DELETE_DATABASE_QUERY + databaseName);
        } catch (SQLException se) {
            Printer.printErrln("SQL Error " + se.getErrorCode() + " "
                    + se.getMessage());
        }
    }

    public static final String GETTING_DRIVER_MESSAGE = "Getting driver...";
    public static final String CONNECTING_MESSAGE = "Connecting to server...";

    private static final String VERSION_QUERY = "Select VERSION()";
    private static final String CREATE_DATABASE_QUERY = "Create DATABASE ";
    private static final String DELETE_DATABASE_QUERY = "Drop DATABASE ";

    private static final String VERSION_NOT_FOUND = "";

    private static final String DRIVER = "com.mysql.jdbc.Driver";

    private final User _user;
    private final String _host;
    private final int _port;
    private final String _url;
}
