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
import utilities.Helper;

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
        Connection con = null;
        DatabaseMetaData metadata = null;

        try {
            Class.forName(_connector.getDriver());
            con = DriverManager.getConnection(_url,
                    _connector.getUser()._username,
                    _connector.getUser()._password);

            metadata = con.getMetaData();

            ResultSet resultSet = metadata.getTables(null, null, "%",
                    new String[]{"TABLE"});

            while (resultSet.next()) {
                tables.add(resultSet.getString(3));
            }
        } catch (SQLException se) {
            Helper.printErrln("SQL Error " + se.getErrorCode() + ' ' + se.getMessage());
        } catch (ClassNotFoundException e) {
            Helper.printErrln("Driver Error: " + e.getMessage());
        } finally {
            //finally block used to close resources
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException se) {
            }//end finally try            
            return tables;
        }//end try
    }

    public void createTable(final String tableName) {
        final String query = "CREATE TABLE " + tableName
                + " (id BIGINT NOT NULL, "
                + "createdAt DATE NOT NULL, "
                + "screenName TINYTEXT NOT NULL, "
                + "fullTweet VARCHAR (255) NOT NULL, "
                + "editedTweet VARCHAR (255) NOT NULL,"
                + "hashtags VARCHAR (255) NOT NULL,"
                + "PRIMARY KEY (id)) ";

        Connection con = null;
        Statement stmt = null;

        try {
            Helper.println("Getting driver...");
            Class.forName(_connector.getDriver());

            Helper.println("Connecting to database...");
            con = DriverManager.getConnection(_url,
                    _connector.getUser()._username,
                    _connector.getUser()._password);
            stmt = con.createStatement();

            Helper.println("Creating table...");
            stmt.executeUpdate(query);

        } catch (SQLException se) {
            Helper.printErrln("SQL Error " + se.getErrorCode() + ' ' + se.getMessage());
        } catch (ClassNotFoundException e) {
            Helper.printErrln("Driver Error: " + e.getMessage());
        } finally {
            //finally block used to close resources
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException se) {
            }// do nothing
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se) {
            }//end finally try
        }//end try
    }

    public void deleteTable(final String table) {
        final String query = "DROP TABLE " + table;

        Connection con = null;
        Statement stmt = null;

        try {
            Helper.println("Getting driver...");
            Class.forName(_connector.getDriver());

            Helper.println("Connecting to database...");
            con = DriverManager.getConnection(_url,
                    _connector.getUser()._username,
                    _connector.getUser()._password);
            stmt = con.createStatement();

            Helper.println("Deleting table...");
            stmt.executeUpdate(query);
        } catch (SQLException se) {
            Helper.printErrln("SQL Error " + se.getErrorCode() + ' ' + se.getMessage());
        } catch (ClassNotFoundException e) {
            Helper.printErrln("Driver Error: " + e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException se) {
            }// do nothing
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se) {
            }//end finally try
        }//end try
    }

    public void insert(List<Status> statuses, final String tableName) {
        String query = "INSERT INTO " + tableName
                + " (id, createdAt, screenName, fullTweet, editedTweet, hashtags) "
                + "VALUES (?,?,?,?,?,?)";

        Connection con = null;
        PreparedStatement pst = null;
        int linesNum = 0;
        try {
            Class.forName(_connector.getDriver());
            con = DriverManager.getConnection(_url,
                    _connector.getUser()._username,
                    _connector.getUser()._password);
            pst = con.prepareStatement(query);

            for (Status status : statuses) {
                if (!rowExists(status, con, tableName)) {
                    pst.setLong(1, status.getId());
                    pst.setDate(2, new java.sql.Date(status.getCreatedAt().getTime()));
                    pst.setString(3, status.getUser().getScreenName());
                    pst.setString(4, getTweetText(status));
                    pst.setString(5, Helper.tweetToWords(getTweetText(status)));
                    pst.setString(6, Helper.hashtagsToString(status.getHashtagEntities()));

                    pst.executeUpdate();
                    linesNum++;
                }
            }
            Helper.println("Total tweets inserted into the Database: " + linesNum);

        } catch (SQLException se) {
            Helper.printErrln("SQL Error " + se.getErrorCode() + ' ' + se.getMessage());
        } catch (ClassNotFoundException e) {
            Helper.printErrln("Driver Error: " + e.getMessage());
        } finally {
            //finally block used to close resources
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (SQLException se) {
            }// do nothing
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException se) {
            }//end finally try
        }//end try
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

            Helper.println("Executing query...");
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                editedText.add(rs.getString(field));
            }
        } catch (SQLException se) {
            Helper.printErrln("SQL Error: " + se.getErrorCode() + " " + se.getMessage());
            System.out.println();
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
            Helper.printErrln("SQL Error: " + se.getErrorCode() + " " + se.getMessage());
            System.out.println();
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

    private String _name;
    private Connector _connector;
    private String _url;
    private Status test;
}
