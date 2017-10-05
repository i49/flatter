package io.github.i49.flatter.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextRenderer implements Renderer {
    
    private static final Pattern HALF_CHAR = Pattern.compile("[\\u0020-\\u00ff]");
    
    private final int maxLineLength;
    private final String indent;
    
    public TextRenderer(int maxLineLength, String indent) {
        this.maxLineLength = maxLineLength;
        this.indent = indent;
    }

    @Override
    public List<String> render(List<Block> blocks) {
        List<String> lines = new ArrayList<>();
        for (Block block: blocks) {
            switch (block.getType()) {
            case BLANK:
                renderBlank(lines);
                break;
            case TOP_RULE:
            case BOTTOM_RULE:
                renderHorizontalRule(block, lines);
                break;
            case HEADING:
                renderHeading(block, lines);
                break;
            case PARAGRAPH:
                renderParagraph(block, lines);
                break;
            case LIST_ITEM:
                renderRawLine(block, lines);
                break;
            case TABLE_ROW:
                renderRawLine(block, lines);
                break;
            }
        }
        return lines;
    }

    private void renderBlank(List<String> lines) {
        if (lines.isEmpty()) {
            return;
        }
        String line = lines.get(lines.size() - 1);
        if (!line.isEmpty()) {
            lines.add("");
        }
    }
    
    private void renderHorizontalRule(Block block, List<String> lines) {
        if (block.getType() == BlockType.TOP_RULE) {
            insertBreak(lines);
        }
        char code = block.getFirstChar();
        StringBuilder b = new StringBuilder();
        int counter = this.maxLineLength;
        while (counter-- > 0) {
            b.append(code);
        }
        lines.add(b.toString());
    }

    private void renderHeading(Block block, List<String> lines) {
        insertBreak(lines);
        lines.add(toFullWidth(block.getContent()));
    }
   
    private void renderParagraph(Block block, List<String> lines) {
        final String content = toFullWidth(block.getContent());
        final int indentSize = this.indent.length();
        for (int index = 0; index < content.length(); ) {
            int length = this.maxLineLength - indentSize;
            if (index + length > content.length()) {
                length = content.length() - index;
            } else if (index + length < content.length()) {
                char next = content.charAt(index + length);
                if (Letters.isPunctuation(next)) {
                    ++length;
                }
            }
            StringBuilder b = new StringBuilder(this.indent);
            b.append(content.substring(index, index + length));
            lines.add(b.toString());
            index += length;
        }
    }
    
    private void renderRawLine(Block block, List<String> lines) {
        StringBuilder b = new StringBuilder(this.indent);
        b.append(block.getContent());
        lines.add(toFullWidth(b.toString()));
    }
    
    private static void insertBreak(List<String> lines) {
        if (lines.isEmpty()) {
            return;
        }
        String lastLine = lines.get(lines.size() - 1);
        if (!lastLine.isEmpty() && !Lines.isHorizontalRule(lastLine)) {
            lines.add("");
        }
    }
    
    private static String toFullWidth(String content) {
        Matcher m = HALF_CHAR.matcher(content);
        StringBuffer b = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(b, mapHalfToFull(m.group()));
        }
        m.appendTail(b);
        return b.toString();
    }
    
    private static String mapHalfToFull(String half) {
        int h = half.charAt(0);
        int f = mapHalfToFull(h);
        return String.valueOf((char)f);
    }
    
    private static int mapHalfToFull(int c) {
        if (c == '\u0020') {
            // space
            return '\u3000';
        } else if (c == '\\') {
            // yen sign
            return '\uffe5';
        } else {
            return 0xff00 + (c - 0x0020); 
        }
    }
}
