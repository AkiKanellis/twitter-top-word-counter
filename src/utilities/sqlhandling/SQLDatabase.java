/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities.sqlhandling;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import twitter4j.Status;
import utilities.generalutils.Converter;
import utilities.generalutils.Printer;
import utilities.tweetshandling.TweetCleaning;

/**
 *
 * @author Dimitrios
 */
public class SQLDatabase {

    public SQLDatabase(final String name, final Connector connector) {
        _name = name;
        _connector = connector;
        _url = connector.getURL() + name + "?characterEncoding=UTF-8";
    }

    public String getName() {
        return _name;
    }

    public void changeName(final String newName) {
        _name = newName;
        _url = _connector.getURL() + newName;
    }

    public List<String> getTables() {
        List<String> tables = new ArrayList<>();

        Printer.println("Getting driver...");
        try {
            Class.forName(_connector.getDriver());
        } catch (ClassNotFoundException e) {
            Printer.printErrln("Driver Error: " + e.getMessage());
            return tables;
        }

        Printer.println("Connecting to database...");
        try (Connection con = DriverManager.getConnection(_url,
                _connector.getUser()._username,
                _connector.getUser()._password);) {

            Printer.println("Retrieving tables...");
            DatabaseMetaData metadata = con.getMetaData();
            try (ResultSet rs = metadata.getTables(null, null, "%",
                    new String[]{"TABLE"});) {
                while (rs.next()) {
                    tables.add(rs.getString(3));
                }
                return tables;
            }
        } catch (SQLException se) {
            Printer.printErrln("SQL Error " + se.getErrorCode() + ' ' + se.getMessage());
            return tables;
        }
    }

    public void createTable(final String tableName) {
        Printer.println("Getting driver...");
        try {
            Class.forName(_connector.getDriver());
        } catch (ClassNotFoundException e) {
            Printer.printErrln("Driver Error: " + e.getMessage());
        }

        Printer.println("Connecting to database...");
        try (Connection con = DriverManager.getConnection(_url,
                _connector.getUser()._username,
                _connector.getUser()._password);
                Statement stmt = con.createStatement()) {

            Printer.println("Creating table...");
            stmt.executeUpdate(CREATE_TABLE_QUERY + tableName + TABLE_COLUMNS);
        } catch (SQLException se) {
            Printer.printErrln("SQL Error " + se.getErrorCode() + ' ' + se.getMessage());
        }
    }

    public void deleteTable(final String tableName) {
        Printer.println("Getting driver...");
        try {
            Class.forName(_connector.getDriver());
        } catch (ClassNotFoundException e) {
            Printer.printErrln("Driver Error: " + e.getMessage());
        }

        Printer.println("Connecting to database...");
        try (Connection con = DriverManager.getConnection(_url,
                _connector.getUser()._username,
                _connector.getUser()._password);
                Statement stmt = con.createStatement()) {

            Printer.println("Deleting table...");
            stmt.executeUpdate(DELETE_TABLE_QUERY + tableName);
        } catch (SQLException se) {
            Printer.printErrln("SQL Error " + se.getErrorCode() + ' ' + se.getMessage());
        }
    }

    public void insert(List<Status> statuses, final String tableName) {
        Printer.println("Getting driver...");
        try {
            Class.forName(_connector.getDriver());
        } catch (ClassNotFoundException e) {
            Printer.printErrln("Driver Error: " + e.getMessage());
        }
        try (Connection con = DriverManager.getConnection(_url,
                _connector.getUser()._username,
                _connector.getUser()._password);
                PreparedStatement pst = con.prepareStatement(INSERT_INTO_QUERY + tableName + PST_COLUMNS);) {

            int linesNum = 0;
            for (Status status : statuses) {
                if (!rowExists(status, con, tableName)) {
                    pst.setLong(1, status.getId());
                    pst.setDate(2, new java.sql.Date(status.getCreatedAt().getTime()));
                    pst.setString(3, status.getUser().getScreenName());
                    pst.setString(4, getTweetText(status));
                    pst.setString(5, TweetCleaning.tweetToWords(getTweetText(status)));
                    pst.setString(6, Converter.hashtagArrayToString(status.getHashtagEntities()));

                    pst.executeUpdate();
                    linesNum++;
                }
            }
            Printer.println("Total tweets inserted into the database: " + linesNum);
        } catch (SQLException se) {
            Printer.printErrln("SQL Error " + se.getErrorCode() + ' ' + se.getMessage());
        }
    }

    public List<String> getField(final String field, final String tableName) {
        List<String> editedText = new ArrayList<>();
        final String query = "SELECT " + field + " FROM " + tableName;
        //+ " WHERE `id` = '$eventid' ";
        Connection con = null;
        Statement stmt = null;

        try {
            Class.forName(_connector.getDriver());
            con = DriverManager.getConnection(_url,
                    _connector.getUser()._username,
                    _connector.getUser()._password);
            stmt = con.prepareStatement(query);

            Printer.println("Executing query...");
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                editedText.add(rs.getString(field));
            }
        } catch (SQLException se) {
            Printer.printErrln("SQL Error: " + se.getErrorCode() + " " + se.getMessage());
            System.out.println();
        } catch (ClassNotFoundException e) {
            Printer.printErrln("Driver Error: " + e.getMessage());
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
            return editedText;
        }//end try
    }

    private boolean rowExists(final Status status, final Connection con,
            final String tableName) throws SQLException {
        final String query = "SELECT *"
                + " FROM " + tableName
                + " WHERE id = '" + status.getId() + "'";
        final PreparedStatement ps = con.prepareStatement(query);
        final ResultSet resultSet = ps.executeQuery();

        return resultSet.next();
    }

    public int getRowsCount(final String tableName) {
        int rowsCount = -1;
        final String query = "SELECT COUNT(*) FROM " + tableName;
        Connection con = null;
        PreparedStatement stmt = null;
        try {
            Class.forName(_connector.getDriver());
            con = DriverManager.getConnection(_url,
                    _connector.getUser()._username,
                    _connector.getUser()._password);
            stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            rowsCount = rs.getInt(1);
        } catch (SQLException se) {
            Printer.printErrln("SQL Error: " + se.getErrorCode() + " " + se.getMessage());
            System.out.println();
        } catch (ClassNotFoundException e) {
            Printer.printErrln("Driver Error: " + e.getMessage());
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
            return rowsCount;
        }//end try
    }

    private static String getTweetText(Status status) {
        if (status.isRetweet()) {
            return status.getRetweetedStatus().getText().replaceAll("[^\\p{ASCII}]", " ");
        } else {
            return status.getText().replaceAll("[^\\p{ASCII}]", " ");
        }
    }

    private static final String CREATE_TABLE_QUERY = "Create TABLE ";
    private static final String TABLE_COLUMNS
            = " (id BIGINT NOT NULL, "
            + "createdAt DATE NOT NULL, "
            + "screenName TINYTEXT NOT NULL, "
            + "fullTweet VARCHAR (255) NOT NULL, "
            + "editedTweet VARCHAR (255) NOT NULL,"
            + "hashtags VARCHAR (255) NOT NULL,"
            + "PRIMARY KEY (id)) ";
    private static final String DELETE_TABLE_QUERY = "Drop TABLE ";
    private static final String INSERT_INTO_QUERY = "INSERT INTO ";
    private static final String PST_COLUMNS = " (id, createdAt, screenName, fullTweet, editedTweet, hashtags) "
            + "VALUES (?,?,?,?,?,?)";

    private String _name;
    private Connector _connector;
    private String _url;
    private Status test;
}
