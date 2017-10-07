package io.github.i49.flatter.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.i49.flatter.base.Letters;
import io.github.i49.flatter.models.Article;
import io.github.i49.flatter.models.Blank;
import io.github.i49.flatter.models.Document;
import io.github.i49.flatter.models.Footer;
import io.github.i49.flatter.models.Heading;
import io.github.i49.flatter.models.Paragraph;
import io.github.i49.flatter.models.Preformatted;
import io.github.i49.flatter.models.Section;
import io.github.i49.flatter.models.Visitor;

public class TextRenderer implements Renderer, Visitor {

    private static final Pattern HALF_CHAR = Pattern.compile("[\\u0020-\\u00ff]");
   
    private static final char BOLD_LINE = '\u2501';
    private static final char UNDERLINE = '\uffe3';
    
    private final int maxLineLength;
    private final String indent;
    
    private final List<String> result = new ArrayList<>();
    private int level;

    public TextRenderer(int maxLineLength, String indent) {
        this.maxLineLength = maxLineLength;
        this.indent = indent;
        this.level = 0;
    }
    
    @Override
    public List<String> render(Document doc) {
        this.level = 0;
        doc.accept(this);
        return result;
    }

    @Override
    public void visit(Article article) {
        ++level;
    }
    
    @Override
    public void visit(Blank blank) {
        addBlankLine();
    }
    
    @Override
    public void visit(Footer footer) {
        addBlankLine();
        addHoritontalRule(BOLD_LINE);
        renderContent(footer.toString());
    }

    @Override
    public void visit(Heading heading) {
        if (this.level <= 1) {
            // heading of level 1
            addBlankLine();
            addHoritontalRule(BOLD_LINE);
            addLine(toFullWidth(heading.toString()));
            addHoritontalRule(BOLD_LINE);
            addBlankLine();
        } else {
            // heading of level 2
            addBlankLine();
            addLine(toFullWidth(heading.toString()));
            addHoritontalRule(UNDERLINE);
        }
    }

    @Override
    public void visit(Paragraph paragraph) {
        String indent = (level > 1) ? this.indent : "";
        renderIndentedContent(paragraph.toString(), indent);
    }
    
    @Override
    public void visit(Preformatted pre) {
        pre.forEach(this::addLine);
    }

    @Override
    public void visit(Section section) {
        ++level;
    }

    @Override
    public void leave(Article section) {
        --level;
    }

    @Override
    public void leave(Section section) {
        --level;
    }
    
    private void renderContent(String content) {
        renderIndentedContent(content, "");
    }
    
    private void renderIndentedContent(String content, String indent) {
        String full = toFullWidth(content);
        final int indentSize = indent.length();
        for (int index = 0; index < full.length(); ) {
            int length = this.maxLineLength - indentSize;
            if (index + length > full.length()) {
                length = full.length() - index;
            } else if (index + length < full.length()) {
                char next = full.charAt(index + length);
                if (Letters.isPunctuation(next)) {
                    ++length;
                }
            }
            StringBuilder b = new StringBuilder(indent);
            b.append(full.substring(index, index + length));
            addLine(b.toString());
            index += length;
        }
    }
    
    private void addLine(String line) {
        this.result.add(line);
    }

    private void addBlankLine() {
        if (result.isEmpty() || !result.get(result.size() - 1).isEmpty()) {
            addLine("");
        }
    }
    
    private void addHoritontalRule(char c) {
        StringBuilder b = new StringBuilder();
        int count = this.maxLineLength;
        while (count-- > 0) {
            b.append(c);
        }
        addLine(b.toString());
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
