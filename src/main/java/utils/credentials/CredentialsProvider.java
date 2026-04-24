package utils.credentials;

import utils.env.EnvUtil;

public class CredentialsProvider {


    /**
     * Returns the credentials associated with the supplied role option.
     *
     * @param key the credential option that identifies which credentials to load
     * @return the resolved credentials for the requested role
     */
    public static Credentials getCredentials(CredentialsOptions key) {

        switch (key) {
            case ADMIN:
                return new Credentials(
                        EnvUtil.get("ADMIN_USERNAME"),
                        EnvUtil.get("ADMIN_PASSWORD")
                );

            case AGENT:
                return new Credentials(
                        EnvUtil.get("AGENT_USERNAME"),
                        EnvUtil.get("AGENT_PASSWORD")
                );

            case CUSTOMER:
            default:
                return new Credentials(
                        EnvUtil.get("CUSTOMER_USERNAME"),
                        EnvUtil.get("CUSTOMER_PASSWORD")
                );
        }
    }
}
