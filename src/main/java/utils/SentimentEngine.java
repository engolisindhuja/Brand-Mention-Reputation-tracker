package utils;

public class SentimentEngine {

    public static String analyze(String text) {
        String lower = text.toLowerCase();

        if (lower.contains("good") || lower.contains("love") || lower.contains("great") || lower.contains("amazing"))
            return "positive";

        if (lower.contains("bad") || lower.contains("hate") || lower.contains("poor") || lower.contains("terrible"))
            return "negative";

        return "neutral";
    }
}
