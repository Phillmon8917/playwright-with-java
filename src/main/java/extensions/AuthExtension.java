package extensions;

import auth.Auth;
import auth.AuthManager;
import auth.Role;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.reflect.Method;

public class AuthExtension implements BeforeEachCallback {

    /**
     * Executes before each test method to ensure authentication based on @Auth
     * annotation. Resolves the role from the test method or class and ensures
     * the user is authenticated with that role.
     *
     * @param context the extension context containing test method and class
     * information
     */
    @Override
    public void beforeEach(ExtensionContext context) {
        Role role = resolveRole(context);
        AuthManager.ensureAuthenticated(role);
    }

    /**
     * Resolves the role from @Auth annotation on the test method, test class,
     * or parent classes. Checks annotations in the following order: method,
     * class, parent classes. If no @Auth annotation is found, returns the
     * default GUEST role.
     *
     * @param context the extension context containing test method and class
     * information
     * @return the resolved role, or GUEST if no @Auth annotation is found
     */
    private Role resolveRole(ExtensionContext context) {
        Method method = context.getRequiredTestMethod();
        Auth methodAuth = method.getAnnotation(Auth.class);
        if (methodAuth != null) {
            return methodAuth.role();
        }

        Class<?> testClass = context.getRequiredTestClass();
        Auth classAuth = testClass.getAnnotation(Auth.class);
        if (classAuth != null) {
            return classAuth.role();
        }

        Class<?> superClass = testClass.getSuperclass();
        while (superClass != null && superClass != Object.class) {
            Auth superAuth = superClass.getAnnotation(Auth.class);
            if (superAuth != null) {
                return superAuth.role();
            }
            superClass = superClass.getSuperclass();
        }

        return Role.GUEST;
    }
}
