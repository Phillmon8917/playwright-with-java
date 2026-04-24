package utils.logger;

import org.apache.logging.log4j.core.*;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.*;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.util.concurrent.ConcurrentHashMap;

@Plugin(name = "AllureLogAppender", category = Core.CATEGORY_NAME, elementType = Appender.ELEMENT_TYPE)
public class AllureLogAppender extends AbstractAppender {

    private static final ConcurrentHashMap<Long, StringBuilder> buffers = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Long, Long> workerToOwner = new ConcurrentHashMap<>();

    protected AllureLogAppender() {
        super("AllureLogAppender", null,
                PatternLayout.newBuilder()
                        .withPattern("[%d{yyyy-MM-dd HH:mm:ss}] [%p] %m%n")
                        .build(), true, null);
    }

    /**
     * Appends the current log event to the buffer associated with the owner thread.
     *
     * @param event the log event to append
     */
    @Override
    public void append(LogEvent event) {
        long threadId = event.getThreadId();
        Long ownerThread = workerToOwner.get(threadId);
        long bufferKey = ownerThread != null ? ownerThread : threadId;
        buffers.computeIfAbsent(bufferKey, k -> new StringBuilder())
                .append(new String(getLayout().toByteArray(event)));
    }

    /**
     * Clears any buffered logs for the supplied owner thread before a new test execution begins.
     *
     * @param ownerThreadId the owner thread whose existing buffer should be cleared
     */
    public static void registerThread(long ownerThreadId) {
        buffers.remove(ownerThreadId);
    }

    /**
     * Associates a worker thread with an owner thread so log events can be buffered together.
     *
     * @param workerThreadId the worker thread identifier
     * @param ownerThreadId the owner thread identifier
     */
    public static void registerWorkerForOwner(long workerThreadId, long ownerThreadId) {
        workerToOwner.put(workerThreadId, ownerThreadId);
    }

    /**
     * Removes the owner-thread mapping for the supplied worker thread.
     *
     * @param workerThreadId the worker thread identifier to unregister
     */
    public static void unregisterWorker(long workerThreadId) {
        workerToOwner.remove(workerThreadId);
    }

    /**
     * Returns and clears the buffered logs for the supplied owner thread.
     *
     * @param ownerThreadId the owner thread whose logs should be drained
     * @return the buffered log output, or an empty string when no logs exist
     */
    public static String drainLogs(long ownerThreadId) {
        StringBuilder sb = buffers.remove(ownerThreadId);
        if (sb == null) return "";
        return sb.toString();
    }
}
