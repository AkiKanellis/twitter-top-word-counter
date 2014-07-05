package utilities.sqlhandling;

public class User {
    
    public User(final String username, final String password) {
        _username = username;
        _password = password;
    }
    
    public User(User user){
        _username = user._username;
        _password = user._password;
    }

    public String _username;
    public String _password;
}
