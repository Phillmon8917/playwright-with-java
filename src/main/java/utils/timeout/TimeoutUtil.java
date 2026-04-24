package utils.timeout;

import utils.env.EnvUtil;

public class TimeoutUtil {

    private static final boolean IS_CI = !EnvUtil.get("CI").isEmpty();

    /**
     * Returns a timeout value adjusted for CI execution when applicable.
     *
     * @param milliseconds the base timeout in milliseconds
     * @return the adjusted timeout in milliseconds
     */
    public static int of(int milliseconds) {
        return IS_CI ? milliseconds * 2 : milliseconds;
    }
}
