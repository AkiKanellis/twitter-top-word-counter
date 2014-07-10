package utilities.sqlhandling;

/**
 * Used for storing the present user.
 *
 * @author Kanellis Dimitris
 */
public class User {

    /**
     * The constructor initialises the variables to the given values.
     *
     * @param username the username of the user
     * @param password the password of the user
     */
    public User(final String username, final String password) {
        _username = username;
        _password = password;
    }

    /**
     *
     * @return the username of the user
     */
    public String getUsername() {
        return _username;
    }

    /**
     *
     * @return the password of the user
     */
    public String getPassword() {
        return _password;
    }

    private final String _username;
    private final String _password;
}
