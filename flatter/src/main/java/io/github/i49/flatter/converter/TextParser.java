package io.github.i49.flatter.converter;

import java.util.List;

import io.github.i49.flatter.base.Lines;
import io.github.i49.flatter.models.Article;
import io.github.i49.flatter.models.Block;
import io.github.i49.flatter.models.Document;
import io.github.i49.flatter.models.Footer;
import io.github.i49.flatter.models.Heading;
import io.github.i49.flatter.models.Paragraph;
import io.github.i49.flatter.models.Preformatted;
import io.github.i49.flatter.models.Section;

public class TextParser implements Parser {

/*    
    @Override
    public List<Block> parse(List<String> lines) {
        List<Block> blocks = new ArrayList<>();
        Block b = null;
        for (String line: lines) {
            if (Lines.isBlank(line)) {
                if (b == null) {
                    blocks.add(Block.BLANK);
                } else {
                    // end of paragraph
                    blocks.add(b);
                    b = null;
                }
            } else if (Lines.isHorizontalRule(line)) {
                if (b == null) {
                    blocks.add(new Block(BlockType.TOP_RULE, line));
                } else {
                    b.setType(BlockType.HEADING);
                    blocks.add(b);
                    b = null;
                    blocks.add(new Block(BlockType.BOTTOM_RULE, line));
                }
            } else if (Lines.isListItem(line)) {
                if (b != null) {
                    blocks.add(b);
                    b = null;
                }
                blocks.add(new Block(BlockType.LIST_ITEM, line));
            } else if (Lines.isTable(line)) {
                if (b != null) {
                    blocks.add(b);
                    b = null;
                }
                blocks.add(new Block(BlockType.TABLE_ROW, line));
            } else {
                // paragraph
                if (b == null) {
                    b = new Block();
                }
                b.append(line);
            }
        }
        if (b != null) {
            blocks.add(b);
        }
        return blocks;
    }
*/
    
    private static final String BOLD_LINE = "\u2501";
    private static final String UNDERLINE = "\uffe3";
    
    @Override
    public Document parse(List<String> lines) {
        return parseDocument(new Reader(lines));
    }
    
    private Document parseDocument(Reader reader) {
        Document doc = new Document();
        while (reader.hasNext()) {
            String line = reader.peekLine();
            if (line.startsWith(BOLD_LINE)) {
                doc.appendChild(parseSection(reader));
            } else {
                doc.appendChild(parsePreformatted(reader));
            }
        }
        return doc;
    }
    
    private Block parseArticle(Reader reader) {
        Article article = new Article();
        String line = reader.nextLine();
        // Skips underline.
        reader.nextLine();
        String title = Lines.trim(line);
        article.appendChild(new Heading(title));
        while (reader.hasNext()) {
            article.appendChild(parseParagraph(reader));
            line = reader.nextNonBlankLine();
            if (line.startsWith(BOLD_LINE)) {
                reader.rewind();
                break;
            }
            String nextLine = reader.peekLine();
            reader.rewind();
            if (nextLine.startsWith(UNDERLINE)) {
                break;
            }
        }
        return article;
    }
    
    private Paragraph parseParagraph(Reader reader) {
        Paragraph p = new Paragraph();
        while (reader.hasNext()) {
            String line = reader.nextLine();
            if (Lines.isBlank(line)) {
                break;
            }
            p.append(line);
        }
        return p;
    }
    
    private Block parseSection(Reader reader) {
        reader.nextLine();
        String text = Lines.trim(reader.nextLine());
        if (!reader.hasNext()) {
            return new Footer(text);
        }
        reader.nextLine();
        Section section = new Section();
        section.appendChild(new Heading(text));
        while (reader.hasNext()) {
            String line = reader.nextNonBlankLine();
            if (line.startsWith(BOLD_LINE)) {
                reader.rewind();
                break;
            }
            String nextLine = reader.peekLine();
            reader.rewind();
            if (nextLine.startsWith(UNDERLINE)) {
                section.appendChild(parseArticle(reader));
            } else {
                section.appendChild(parsePreformatted(reader));
                break;
            }
        }
        return section;
    }
    
    private Preformatted parsePreformatted(Reader reader) {
        Preformatted pre = new Preformatted();
        while (reader.hasNext()) {
            String line = reader.nextLine();
            if (line.startsWith(BOLD_LINE)) {
                reader.rewind();
                break;
            }
            pre.append(line);
        }
        return pre;
    }
    
    private static class Reader {
        
        private final List<String> lines;
        private int nextIndex;
        
        public Reader(List<String> lines) {
            this.lines = lines;
        }
        
        public boolean hasNext() {
            return nextIndex < lines.size();
        }
        
        public String nextLine() {
            if (!hasNext()) {
                return null;
            }
            String line = peekLine();
            ++nextIndex;
            return line;
        }
        
        public String nextNonBlankLine() {
            while (hasNext()) {
                String line = nextLine();
                if (!Lines.isBlank(line)) {
                    return line;
                }
            }
            return null;
        }
        
        public String peekLine() {
            return lines.get(nextIndex);
        }
        
        public Reader rewind() {
            if (nextIndex > 0) {
                --nextIndex;
            }
            return this;
        }
    }
}
