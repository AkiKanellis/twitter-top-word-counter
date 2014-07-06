/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 *
 * @author Dimitrios
 */
public class Connector {

    public Connector(final User user, final String host, final int port) {
        _user = user;
        _host = host;
        _port = port;
        _url = "jdbc:mysql://" + _host + ":" + _port + "/";
    }

    public User getUser() {
        return _user;
    }

    public String getURL() {
        return _url;
    }

    public String getDriver() {
        return DRIVER;
    }

    public String getVersion() {
        Printer.println("Getting driver...");
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            Printer.printErrln("Driver Error: " + e.getMessage());
            return VERSION_NOT_FOUND;
        }

        Printer.println("Connecting to database...");
        try (Connection con = DriverManager.getConnection(_url,
                _user.getUsername(),
                _user.getPassword());
                Statement stmt = con.createStatement();) {

            Printer.println("Executing query...");
            try (ResultSet rs = stmt.executeQuery(VERSION_QUERY);) {
                rs.next();
                return rs.getString(1);
            }
        } catch (SQLException se) {
            Printer.printErrln("SQL Error: " + se.getErrorCode() + ' ' + se.getMessage());
            return VERSION_NOT_FOUND;
        }
    }

    public List<String> getDatabases() {
        List<String> databases = new ArrayList<>();

        Printer.println("Getting driver...");
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            Printer.printErrln("Driver Error: " + e.getMessage());
            return databases;
        }

        Printer.println("Connecting to database...");
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
            Printer.printErrln("SQL Error: " + se.getErrorCode() + " " + se.getMessage());
            return databases;
        }
    }

    public void createDatabase(final String databaseName) {
        Printer.println("Getting driver...");
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            Printer.printErrln("Driver Error: " + e.getMessage());
        }

        Printer.println("Connecting to database...");
        try (Connection con = DriverManager.getConnection(_url,
                _user.getUsername(),
                _user.getPassword());
                Statement stmt = con.createStatement();) {
            Printer.println("Creating Database...");
            stmt.executeUpdate(CREATE_DATABASE_QUERY + databaseName);
        } catch (SQLException se) {
            Printer.printErrln("SQL Error " + se.getErrorCode() + ' ' + se.getMessage());
        }
    }

    public void deleteDatabase(final String databaseName) {
        Printer.println("Getting driver...");
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            Printer.printErrln("Driver Error: " + e.getMessage());
        }

        Printer.println("Connecting to database...");
        try (Connection con = DriverManager.getConnection(_url,
                _user.getUsername(),
                _user.getPassword());
                Statement stmt = con.createStatement();) {

            Printer.println("Deleting Database...");
            stmt.executeUpdate(DELETE_DATABASE_QUERY + databaseName);
        } catch (SQLException se) {
            Printer.printErrln("SQL Error " + se.getErrorCode() + ' ' + se.getMessage());
        }
    }

    private static final String VERSION_QUERY = "Select VERSION()";
    private static final String CREATE_DATABASE_QUERY = "Create DATABASE ";
    private static final String DELETE_DATABASE_QUERY = "Drop DATABASE ";

    private static final String VERSION_NOT_FOUND = "";

    private User _user;
    public int _port;
    public String _host;
    private final String DRIVER = "com.mysql.jdbc.Driver";
    private String _url;
}
