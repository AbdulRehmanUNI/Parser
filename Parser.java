import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.io.IOException;

public class Parser {
    private static final Set<String> KEYWORDS = new HashSet<>(Arrays.asList("if", "else", "while", "int", "float"));
    private static final Set<String> OPERATORS = new HashSet<>(Arrays.asList("+", "-", "*", "/"));
    private static final Set<String> PUNCTUATION = new HashSet<>(Arrays.asList("(", ")", "{", "}", ";"));

    public static void tokenize(String input) {
        String[] parts = input.split("\\s+");
        Map<String, String> tokens = new LinkedHashMap<>();

        for (String part : parts) {
            if (KEYWORDS.contains(part)) {
                tokens.put(part, "KEYWORD");
            } else if (OPERATORS.contains(part)) {
                tokens.put(part, "OPERATOR");
            } else if (PUNCTUATION.contains(part)) {
                tokens.put(part, "PUNCTUATION");
            } else if (part.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
                tokens.put(part, "IDENTIFIER");
            } else if (part.matches("[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?")) {
                tokens.put(part, "NUMBER");
            } else {
                tokens.put(part, "UNKNOWN");
            }
        }

        for (Map.Entry<String, String> entry : tokens.entrySet()) {
            System.out.println("<" + entry.getValue() + ", " + entry.getKey() + ">");
        }
    }

    public static void main(String[] args) {
        try {
            String input = new String(Files.readAllBytes(Paths.get("input.txt")));
            tokenize(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}