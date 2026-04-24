package utils.env;

import io.github.cdimascio.dotenv.Dotenv;
import utils.logger.LoggingUtil;

public class EnvUtil {

    private static final Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing()
            .load();

    /**
     * Retrieves a configuration value from the loaded {@code .env} file or system environment variables.
     *
     * @param key the configuration key to resolve
     * @return the resolved value, or an empty string when the key is missing
     */
    public static String get(String key) {

        String value = dotenv.get(key);
        if (value != null && !value.isEmpty()) {
            LoggingUtil.info("[ENV] Found in .env → " + key);
            return value;
        }

        value = System.getenv(key);
        if (value != null && !value.isEmpty()) {
            LoggingUtil.info("[ENV] Found in system env → " + key);
            return value;
        }

        LoggingUtil.warn("[ENV] Missing key → " + key);

        return "";
    }
}
