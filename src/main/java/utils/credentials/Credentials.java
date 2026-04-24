package utils.credentials;

public class Credentials
{
    private final String username;
    private final String password;

    public Credentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Returns the configured username.
     *
     * @return the username value
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the configured password.
     *
     * @return the password value
     */
    public String getPassword() {
        return password;
    }
}
