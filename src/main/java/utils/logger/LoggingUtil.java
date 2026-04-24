package utils.logger;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.*;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;

public class LoggingUtil {

    private static final Logger logger = createLogger();

    /**
     * Builds and initializes the shared Log4j logger configuration used by the test framework.
     *
     * @return the configured logger instance
     */
    private static Logger createLogger() {
        ConfigurationBuilder<BuiltConfiguration> builder =
                ConfigurationBuilderFactory.newConfigurationBuilder();

        builder.setStatusLevel(Level.ERROR);

        LayoutComponentBuilder layout = builder.newLayout("PatternLayout")
                .addAttribute("pattern", "[%d{yyyy-MM-dd HH:mm:ss}] [%p] %m%n");

        AppenderComponentBuilder fileAppender = builder.newAppender("FileAppender", "File")
                .addAttribute("fileName", "logs/test.log")
                .addAttribute("append", false)
                .add(layout);

        builder.add(fileAppender);

        AppenderComponentBuilder consoleAppender = builder.newAppender("Console", "CONSOLE")
                .add(layout);

        builder.add(consoleAppender);

        builder.add(builder.newRootLogger(Level.INFO)
                .add(builder.newAppenderRef("FileAppender"))
                .add(builder.newAppenderRef("Console")));

        Configurator.initialize(builder.build());

        AllureLogAppender allureAppender = new AllureLogAppender();
        allureAppender.start();

        org.apache.logging.log4j.core.Logger rootLogger =
                (org.apache.logging.log4j.core.Logger) LogManager.getRootLogger();
        rootLogger.addAppender(allureAppender);

        return LogManager.getLogger(LoggingUtil.class);
    }

    /**
     * Writes an informational message to the configured log appenders.
     *
     * @param message the message to log
     */
    public static void info(String message) {
        logger.info(message);
    }

    /**
     * Writes a warning message to the configured log appenders.
     *
     * @param message the message to log
     */
    public static void warn(String message) {
        logger.warn(message);
    }

    /**
     * Writes an error message to the configured log appenders.
     *
     * @param message the message to log
     */
    public static void error(String message) {
        logger.error(message);
    }
}
