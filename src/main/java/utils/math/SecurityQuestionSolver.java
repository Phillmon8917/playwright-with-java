package utils.math;

import lookups.numbers.NumberLookup;
import utils.logger.LoggingUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SecurityQuestionSolver {

    /**
     * Parses and solves a simple arithmetic security question expressed with symbols or words.
     *
     * @param question the security question text to solve
     * @return the computed answer as a string
     */
    public static String solve(String question) {
        String lower = question.toLowerCase();

        lower = lower
                .replace("×", " times ")
                .replace("*", " times ")
                .replace("+", " plus ")
                .replaceAll("(?<=\\d)-(?=\\d)", " minus ")
                .replaceAll("(?<=\\s)-(?=\\s)", " minus ")
                .replace("÷", " divided ")
                .replace("/", " divided ");

        lower = lower.replaceAll(
                "(twenty|thirty|forty|fifty|sixty|seventy|eighty|ninety)\\s+(one|two|three|four|five|six|seven|eight|nine)",
                "$1-$2"
        );

        List<Map.Entry<String, Integer>> entries = new ArrayList<>(NumberLookup.NUMBERS.entrySet());
        entries.sort((a, b) -> b.getKey().length() - a.getKey().length());

        for (Map.Entry<String, Integer> entry : entries) {
            lower = lower.replace(entry.getKey(), entry.getValue().toString());
        }

        String op = null;
        if      (lower.contains("plus")      || lower.contains("added"))      op = "add";
        else if (lower.contains("minus")     || lower.contains("subtracted")) op = "subtract";
        else if (lower.contains("times")     || lower.contains("multiplied")) op = "multiply";
        else if (lower.contains("divided"))                                    op = "divide";

        List<Integer> nums = new ArrayList<>();
        Matcher matcher = Pattern.compile("\\d+").matcher(lower);
        while (matcher.find()) {
            nums.add(Integer.parseInt(matcher.group()));
        }

        if (nums.size() >= 2 && op != null) {
            int a = nums.get(0);
            int b = nums.get(1);

            int result = switch (op) {
                case "add"      -> a + b;
                case "subtract" -> a - b;
                case "multiply" -> a * b;
                case "divide"   -> a / b;
                default         -> 0;
            };

            LoggingUtil.info("Security question solved: " + a + " " + op + " " + b + " = " + result);
            return String.valueOf(result);
        }

        LoggingUtil.error("Could not solve security question: " + question);
        throw new RuntimeException("Failed to solve security check question: " + question);
    }
}
