package io.github.i49.flatter.base;

import java.util.regex.Pattern;

public final class Lines {
    
    private static final Pattern BLANK_PATTERN = Pattern.compile("\\h*");
    private static final Pattern HORIZONTAL_RULE_PATTERN = Pattern.compile("\\h*(\\H)\\1+\\h*");
    private static final Pattern LIST_ITEM_PATTERN = Pattern.compile("\\h*\\u2605.+");

    public static boolean isBlank(String line) {
        return BLANK_PATTERN.matcher(line).matches();
    }
    
    public static boolean isHorizontalRule(String line) {
        return HORIZONTAL_RULE_PATTERN.matcher(line).matches();
    }
    
    public static boolean isListItem(String line) {
        return LIST_ITEM_PATTERN.matcher(line).matches();
    }
    
    public static boolean isTable(String line) {
        String trimmed = trimLeft(line);
        if (trimmed.isEmpty()) {
            return false;
        }
        char c = trimmed.charAt(0);
        return '\u2500' <= c && c <= '\u2542';
    }
    
    public static String trimLeft(String line) {
        return line.replaceFirst("^\\h+", "");
    }

    public static String trimRight(String line) {
        return line.replaceFirst("\\h+$", "");
    }

    public static String trim(String line) {
        return trimRight(trimLeft(line));
    }
}
