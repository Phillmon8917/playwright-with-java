package hooks.errors;

public class ErrorStore {

    private static final ThreadLocal<Throwable> STORE = new ThreadLocal<>();

    /**
     * Stores the given throwable in thread-local storage.
     *
     * @param t the throwable to store
     */
    public static void set(Throwable t) {
        STORE.set(t);
    }

    /**
     * Retrieves the stored throwable from thread-local storage.
     *
     * @return the stored throwable, or null if none has been set
     */
    public static Throwable get() {
        return STORE.get();
    }

    /**
     * Clears the stored throwable from thread-local storage.
     */
    public static void clear() {
        STORE.remove();
    }
}
