package utils.randomNumber;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import utils.logger.LoggingUtil;

public class RandomNumberGeneratorUtil {

    /**
     * Generates a random integer within the inclusive range between min and
     * max.
     *
     * @param min the minimum inclusive value
     * @param max the maximum inclusive value
     * @return a random integer between min and max, inclusive
     */
    public static int getRandomNumber(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }

    /**
     * Generates a concatenated sequence of random integers and returns it as an
     * integer.
     *
     * @param count the number of random integers to generate and concatenate
     * @param min the minimum inclusive value for each generated number
     * @param max the maximum inclusive value for each generated number
     * @return the concatenated random numbers parsed as an integer
     */
    public static int getRandomNumbers(int count, int min, int max) {
        StringBuilder randomNumbers = new StringBuilder();
        for (int i = 0; i < count; i++) {
            randomNumbers.append(getRandomNumber(min, max));
        }
        LoggingUtil.info("Generated random numbers: " + randomNumbers.toString());
        return Integer.parseInt(randomNumbers.toString());
    }

    /**
     * Creates a guaranteed unique numeric value based on the current timestamp.
     *
     * @return a unique integer representing the current date and time in
     * yyyyMMddHHmmss format
     */
    public static long getGuaranteedUniqueRandomNumber() {
        LocalDateTime now = LocalDateTime.now();
        String uniqueNumberStr = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        LoggingUtil.info("Generated unique number: " + uniqueNumberStr);
        return Long.parseLong(uniqueNumberStr);
    }

}
