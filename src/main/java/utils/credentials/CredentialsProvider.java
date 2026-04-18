package utils.credentials;

public class CredentialsProvider
{
    /**
     * Retrieves credentials based on the key.
     */
    public static Credentials getCredentials(CredentialsOptions key) {

        switch (key) {
            case ADMIN:
                return new Credentials(
                        System.getenv().getOrDefault("ADMIN_USERNAME", ""),
                        System.getenv().getOrDefault("ADMIN_PASSWORD", "")
                );

            case AGENT:
                return new Credentials(
                        System.getenv().getOrDefault("AGENT_USERNAME", ""),
                        System.getenv().getOrDefault("AGENT_PASSWORD", "")
                );

            case CUSTOMER:
            default:
                return new Credentials(
                        System.getenv().getOrDefault("CUSTOMER_USERNAME", ""),
                        System.getenv().getOrDefault("CUSTOMER_PASSWORD", "")
                );
        }
    }
}
