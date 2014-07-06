package utilities.sqlhandling;

public class User {
    
    public User(final String username, final String password) {
        _username = username;
        _password = password;
    }
    
    public User(User user){
        _username = user.getUsername();
        _password = user.getPassword();
    }

    public String getUsername() {
        return _username;
    }

    public void setUsername(final String username) {
        _username = username;
    }

    public String getPassword() {
        return _password;
    }

    public void setPassword(final String password) {
        _password = password;
    }

    private String _username;
    private String _password;
}
