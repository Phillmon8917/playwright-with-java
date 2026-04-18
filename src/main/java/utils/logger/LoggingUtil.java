package utils.logger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.builder.api.*;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;

public class LoggingUtil
{
    private static final Logger logger = createLogger();

    private static Logger createLogger() {
        ConfigurationBuilder<BuiltConfiguration> builder =
                ConfigurationBuilderFactory.newConfigurationBuilder();

        builder.setStatusLevel(Level.ERROR);

        // Pattern: [timestamp] [LEVEL] message
        LayoutComponentBuilder layout = builder.newLayout("PatternLayout")
                .addAttribute("pattern", "[%d{yyyy-MM-dd HH:mm:ss}] [%p] %m%n");

        // File Appender (overwrite each run)
        AppenderComponentBuilder fileAppender = builder.newAppender("FileAppender", "File")
                .addAttribute("fileName", "logs/test.log")
                .addAttribute("append", false)
                .add(layout);

        builder.add(fileAppender);

        // Console Appender
        AppenderComponentBuilder consoleAppender = builder.newAppender("Console", "CONSOLE")
                .add(layout);

        builder.add(consoleAppender);

        builder.add(builder.newRootLogger(Level.INFO)
                .add(builder.newAppenderRef("FileAppender"))
                .add(builder.newAppenderRef("Console")));

        Configurator.initialize(builder.build());

        return LogManager.getLogger(LoggingUtil.class);
    }

    public static void info(String message) {
        logger.info(message);
    }

    public static void warn(String message) {
        logger.warn(message);
    }

    public static void error(String message) {
        logger.error(message);
    }
}
