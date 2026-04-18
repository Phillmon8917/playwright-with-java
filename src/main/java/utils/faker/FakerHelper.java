package utils.faker;

import com.github.javafaker.Faker;
import java.util.HashMap;
import java.util.Map;

public class FakerHelper
{
    private static final Faker faker = new Faker();

    /**
     * Generates a random first name.
     */
    public static String generateFirstName() {
        return faker.name().firstName();
    }

    /**
     * Generates a random last name.
     */
    public static String generateLastName() {
        return faker.name().lastName();
    }

    /**
     * Generates a random email address.
     */
    public static String generateEmail() {
        return faker.internet().emailAddress();
    }

    /**
     * Generates a random password with a minimum length.
     */
    public static String generatePassword(int minLength) {
        String password = faker.internet().password(minLength, minLength + 4);

        if (password.length() < minLength) {
            password += faker.lorem().characters(minLength - password.length());
        }

        return password;
    }

    /**
     * Overload with default min length = 6
     */
    public static String generatePassword() {
        return generatePassword(6);
    }

    /**
     * Generates customer signup data.
     */
    public static Map<String, String> generateCustomerSignupData() {
        Map<String, String> data = new HashMap<>();

        data.put("firstName", generateFirstName());
        data.put("lastName", generateLastName());
        data.put("email", generateEmail());
        data.put("password", generatePassword(8));

        return data;
    }
}
