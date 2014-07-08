package utilities.sqlhandling;

/**
 *
 * @author Kanellis Dimitris
 */
public class User {
    
    public User(final String username, final String password) {
        _username = username;
        _password = password;
    }

    public String getUsername() {
        return _username;
    }

    public String getPassword() {
        return _password;
    }

    private final String _username;
    private final String _password;
}
