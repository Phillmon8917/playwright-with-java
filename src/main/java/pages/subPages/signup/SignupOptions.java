package pages.subPages.signup;

public record SignupOptions(
        String firstName,
        String lastName,
        String email,
        String password,
        String confirmPassword,
        String securityCheck,
        boolean agreeToTerms,
        boolean expectValidationErrors,
        String fieldId
) {}
