package luckystore.datn.util;

public class JsonString {
    public static String errorToJsonObject(String field, String error) {
        return "\"" + field + "\":\"" + error + "\"";
    }

    public static String stringToJson(String str) {
        return "{" + str + "}";
    }
}
