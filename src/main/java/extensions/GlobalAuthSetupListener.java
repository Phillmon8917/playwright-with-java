package extensions;

import org.junit.platform.launcher.LauncherSession;
import org.junit.platform.launcher.LauncherSessionListener;
import setup.GlobalAuthSetup;

public class GlobalAuthSetupListener implements LauncherSessionListener {

    /**
     * Called once when the launcher session opens, before any test discovery or
     * execution. Delegates immediately to {@link GlobalAuthSetup#run()} which
     * ensures all role storage files exist before any test gets a chance to
     * start.
     *
     * @param session the launcher session that is opening
     */
    @Override
    public void launcherSessionOpened(LauncherSession session) {
        GlobalAuthSetup.run();
    }
}
