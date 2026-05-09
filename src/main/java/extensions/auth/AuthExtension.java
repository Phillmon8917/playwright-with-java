package extensions.auth;

import org.junit.jupiter.api.extension.*;
import utils.logger.LoggingUtil;
import utils.storage.StorageStateManager;

import java.lang.reflect.Method;

public class AuthExtension implements BeforeEachCallback {

    /**
     * Checks the current test for role-based tags and warns when the expected storage state is unavailable.
     *
     * @param context the JUnit extension context for the test about to run
     */
    @Override
    public void beforeEach(ExtensionContext context) {
        Method method = context.getRequiredTestMethod();
        Class<?> testClass = context.getRequiredTestClass();

        boolean needsCustomer = hasTag(testClass, method, "customer");
        boolean needsAgent = hasTag(testClass, method, "agent");
        boolean needsAdmin = hasTag(testClass, method, "admin");

        if (needsCustomer && !StorageStateManager.storageExists("customer")) {
            LoggingUtil.warn("Customer storage not found - authentication will run inline");
        }
        if (needsAgent && !StorageStateManager.storageExists("agent")) {
            LoggingUtil.warn("Agent storage not found - authentication will run inline");
        }
        if (needsAdmin && !StorageStateManager.storageExists("admin")) {
            LoggingUtil.warn("Admin storage not found - authentication will run inline");
        }
    }

    /**
     * Determines whether the supplied test method or its declaring class is annotated with the given tag.
     *
     * @param testClass the test class to inspect for class-level tags
     * @param method the test method to inspect for method-level tags
     * @param tag the tag value to look for
     * @return {@code true} when the tag is present on the method or class; otherwise {@code false}
     */
    private boolean hasTag(Class<?> testClass, Method method, String tag) {
        org.junit.jupiter.api.Tag methodTag = method.getAnnotation(org.junit.jupiter.api.Tag.class);
        org.junit.jupiter.api.Tags methodTags = method.getAnnotation(org.junit.jupiter.api.Tags.class);
        org.junit.jupiter.api.Tag classTag = testClass.getAnnotation(org.junit.jupiter.api.Tag.class);
        org.junit.jupiter.api.Tags classTags = testClass.getAnnotation(org.junit.jupiter.api.Tags.class);

        if (methodTag != null && methodTag.value().equals(tag)) return true;
        if (classTag != null && classTag.value().equals(tag)) return true;

        if (methodTags != null) {
            for (org.junit.jupiter.api.Tag t : methodTags.value()) {
                if (t.value().equals(tag)) return true;
            }
        }
        if (classTags != null) {
            for (org.junit.jupiter.api.Tag t : classTags.value()) {
                if (t.value().equals(tag)) return true;
            }
        }
        return false;
    }
}
