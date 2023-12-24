import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.io.IOException;

public class Parser {
    private static final Set<String> KEYWORDS = new HashSet<>(Arrays.asList("if", "else", "while", "int", "float"));
    private static final Set<String> OPERATORS = new HashSet<>(Arrays.asList("+", "-", "*", "/"));
    private static final Set<String> PUNCTUATION = new HashSet<>(Arrays.asList("(", ")", "{", "}", ";"));

    public static void tokenize(String input) {
        input = input.replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)", "")
                .replaceAll("(\".*?\")", " $1 ")
                .replace("+", " + ")
                .replace("-", " - ")
                .replace("*", " * ")
                .replace("/", " / ")
                .replace("(", " ( ")
                .replace(")", " ) ")
                .replace("{", " { ")
                .replace("}", " } ")
                .replace(";", " ; ");

                

        String[] parts = input.split("\\s+");
        List<String> tokens = new ArrayList<>();

        for (String part : parts) {
            if (part.startsWith("\"") && part.endsWith("\"")) {
                tokens.add(part + " STRING_LITERAL");
            } else if (KEYWORDS.contains(part)) {
                tokens.add(part + " KEYWORD");
            } else if (OPERATORS.contains(part)) {
                tokens.add(part + " OPERATOR");
            } else if (PUNCTUATION.contains(part)) {
                tokens.add(part + " PUNCTUATION");
            } else if (part.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
                tokens.add(part + " IDENTIFIER");
            } else if (part.matches("[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?")) {
                tokens.add(part + " NUMBER");
            } else {
                tokens.add(part + " UNKNOWN");
            }
        }

        for (String token : tokens) {
            System.out.println("< "+token+" >");
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



/*
 * 
 * import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.io.IOException;

public class Parser {
    private static final Set<String> KEYWORDS = new HashSet<>(Arrays.asList("if", "else", "while", "int", "float"));
    private static final Set<String> OPERATORS = new HashSet<>(Arrays.asList("+", "-", "*", "/"));
    private static final Set<String> PUNCTUATION = new HashSet<>(Arrays.asList("(", ")", "{", "}", ";"));

    public static void tokenize(String input) {
        String[] parts = input.split("\\s+");
        Map<String, List<String>> tokens = new LinkedHashMap<>();

        for (String part : parts) {
            if (KEYWORDS.contains(part)) {
                addToMap(tokens, part, "KEYWORD");
            } else if (OPERATORS.contains(part)) {
                addToMap(tokens, part, "OPERATOR");
            } else if (PUNCTUATION.contains(part)) {
                addToMap(tokens, part, "PUNCTUATION");
            } else if (part.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
                addToMap(tokens, part, "IDENTIFIER");
            } else if (part.matches("[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?")) {
                addToMap(tokens, part, "NUMBER");
            } else {
                addToMap(tokens, part, "UNKNOWN");
            }
        }

        for (Map.Entry<String, List<String>> entry : tokens.entrySet()) {
            for (String value : entry.getValue()) {
                System.out.println("<" + value + ", " + entry.getKey() + ">");
            }
        }
    }

    private static void addToMap(Map<String, List<String>> map, String key, String value) {
        map.computeIfAbsent(key, k -> new LinkedList<>()).add(value);
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

 * 
 * 
 */