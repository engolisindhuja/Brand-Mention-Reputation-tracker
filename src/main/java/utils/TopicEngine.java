package utils;

public class TopicEngine {

    public static String detect(String text) {
        if (text == null) return "General";

        String t = text.toLowerCase();

        if (t.contains("deliver") || t.contains("shipping") || t.contains("shipment"))
            return "Delivery";

        if (t.contains("quality") || t.contains("product") || t.contains("defect"))
            return "Product Quality";

        if (t.contains("support") || t.contains("service") || t.contains("help"))
            return "Support";

        if (t.contains("price") || t.contains("cost") || t.contains("expensive") || t.contains("cheap"))
            return "Pricing";

        if (t.contains("amazing") || t.contains("love") || t.contains("great") || t.contains("super"))
            return "Appreciation";

        if (t.contains("worst") || t.contains("bad") || t.contains("terrible") || t.contains("poor"))
            return "Complaint";

        return "General"; 
    }
}
