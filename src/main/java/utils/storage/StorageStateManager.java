package utils.storage;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import utils.logger.LoggingUtil;

public class StorageStateManager {

    private static final String STORAGE_DIR = "storage";

    /**
     * Returns the storage state file path for the supplied role.
     *
     * @param role the role whose storage state path should be resolved
     * @return the path to the role-specific storage state file
     */
    public static Path getStoragePath(String role) {
        return Paths.get(STORAGE_DIR, role + ".json");
    }

    /**
     * Checks whether a storage state file exists for the supplied role.
     *
     * @param role the role whose storage state file should be checked
     * @return {@code true} when the storage state file exists; otherwise
     * {@code false}
     */
    public static boolean storageExists(String role) {
        return Files.exists(getStoragePath(role));
    }

    /**
     * Creates the storage directory when it does not already exist.
     */
    public static void ensureStorageDirectory() {
        try {
            Files.createDirectories(Paths.get(STORAGE_DIR));
        } catch (Exception e) {
            LoggingUtil.error("Failed to create storage directory: " + e.getMessage());
        }
    }

    /**
     * Deletes the storage state file for the supplied role when it exists.
     *
     * @param role the role whose storage state file should be deleted
     */
    public static void deleteStorage(String role) {
        try {
            Files.deleteIfExists(getStoragePath(role));
            LoggingUtil.info("Deleted storage state for: " + role);
        } catch (Exception e) {
            LoggingUtil.error("Failed to delete storage state for " + role + ": " + e.getMessage());
        }
    }

    /**
     * Returns the storage path for the given role, throwing clearly if the file
     * does not exist. Call this from the @Before hook so a missing session
     * produces an immediate, readable failure instead of a silent broken
     * context.
     *
     * @param role the role whose storage state is required
     * @return the path to the storage state file
     * @throws IllegalStateException when the storage file does not exist
     */
    public static Path requireStoragePath(String role) {
        Path path = getStoragePath(role);
        if (!Files.exists(path)) {
            throw new IllegalStateException(
                    "Storage state for role '" + role + "' not found at: " + path.toAbsolutePath()
                    + "\nRun the auth suite (AuthRunner) before running tests tagged @" + role + "."
            );
        }
        return path;
    }
}
