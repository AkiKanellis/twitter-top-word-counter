/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities.sqlhandling;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import utilities.*;

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
        return _driver;
    }

    public String getVersion() {
        String version = new String();
        final String query = "Select VERSION()";
        Connection con = null;
        Statement stmt = null;

        try {
            Helper.println("Getting driver...");
            Class.forName(_driver);

            Helper.println("Connecting to database...");
            con = DriverManager.getConnection(_url, _user._username, _user._password);
            stmt = con.createStatement();

            Helper.println("Executing query...");
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            version = rs.getString(1);
        } catch (SQLException se) {
            Helper.printErrln("SQL Error: " + se.getErrorCode() + ' ' + se.getMessage());
        } catch (ClassNotFoundException e) {
            Helper.printErrln("Driver Error: " + e.getMessage());
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se) {
            }// do nothing
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException se) {
            }//end finally try            
            return version;
        }//end try
    }

    public List<String> getDatabases() {
        List<String> databases = new ArrayList<>();
        Connection con = null;
        Statement stmt = null;

        try {
            Helper.println("Getting driver...");
            Class.forName(_driver);

            Helper.println("Connecting to database...");
            con = DriverManager.getConnection(_url, _user._username, _user._password);
            stmt = con.createStatement();

            Helper.println("Finding Databases...");
            ResultSet rs = con.getMetaData().getCatalogs();

            while (rs.next()) {
                databases.add(rs.getString("TABLE_CAT"));
            }
        } catch (SQLException se) {
            Helper.printErrln("SQL Error: " + se.getErrorCode() + " " + se.getMessage());
        } catch (ClassNotFoundException e) {
            Helper.printErrln("Driver Error: " + e.getMessage());
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se) {
            }// do nothing
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException se) {
            }//end finally try            
            return databases;
        }//end try
    }

    public void createDatabase(final String databaseName) {
        final String query = "CREATE DATABASE " + databaseName;

        Connection con = null;
        Statement stmt = null;

        try {
            Helper.println("Getting driver...");
            Class.forName(_driver);

            Helper.println("Connecting to database...");
            con = DriverManager.getConnection(_url, _user._username, _user._password);
            stmt = con.createStatement();

            Helper.println("Creating Database...");
            stmt.executeUpdate(query);

        } catch (SQLException se) {
            Helper.printErrln("SQL Error " + se.getErrorCode() + ' ' + se.getMessage());
        } catch (ClassNotFoundException e) {
            Helper.printErrln("Driver Error: " + e.getMessage());
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se) {
            }// do nothing
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try  
        }//end try
    }

    public void deleteDatabase(final String databaseName) {
        final String query = "DROP DATABASE " + databaseName;

        Connection con = null;
        Statement stmt = null;

        try {
            Helper.println("Getting driver...");
            Class.forName(_driver);

            Helper.println("Connecting to database...");
            con = DriverManager.getConnection(_url, _user._username, _user._password);
            stmt = con.createStatement();

            Helper.println("Deleting Database...");
            stmt.executeUpdate(query);

        } catch (SQLException se) {
            Helper.printErrln("SQL Error " + se.getErrorCode() + ' ' + se.getMessage());
        } catch (ClassNotFoundException e) {
            Helper.printErrln("Driver Error: " + e.getMessage());
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se) {
            }// do nothing
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try  
        }//end try
    }

    private User _user;
    public int _port;
    public String _host;
    private final String _driver = "com.mysql.jdbc.Driver";
    private String _url;
}
