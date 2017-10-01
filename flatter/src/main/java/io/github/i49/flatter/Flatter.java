package io.github.i49.flatter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Flatter {
    
    private static final Logger log = Logger.getLogger(Flatter.class.getName());
    
    private static final Charset CHARSET = Charset.forName("MS932");
    private static final String SUFFIX = "_out";
    
    private static final int MAX_LINE_LENGTH = 35;
    private static final String INDENT = "\u3000";

    private final SimpleRenderer renderder;
    
    public Flatter() {
        this.renderder = new SimpleRenderer(MAX_LINE_LENGTH, INDENT);
    }
    
    public void flatter(Path input) throws IOException {
        Path output = getOutputPath(input);
        
        log.info("Input file: " + input.toAbsolutePath().toString());
        log.info("Output file: " + output.toAbsolutePath().toString());
        
        List<String> oldLines = Files.readAllLines(input, CHARSET);
        List<Block> blocks = parseLines(oldLines);
        List<String> newLines = renderder.render(blocks);
        Files.write(output, newLines, CHARSET);
    }
    
    private static Path getOutputPath(Path input) {
        String oldName = input.getFileName().toString();
        int lastIndex = oldName.lastIndexOf('.');
        StringBuilder b = new StringBuilder();
        if (lastIndex >= 0) {
            b.append(oldName.substring(0, lastIndex));
            b.append(SUFFIX);
            b.append(oldName.substring(lastIndex));
        } else {
            b.append(oldName).append(SUFFIX);
        }
        final String newName = b.toString();
        Path parent = input.getParent();
        if (parent != null) {
            return parent.resolve(newName);
        } else {
            return Paths.get(newName);
        }
    }
    
    private List<Block> parseLines(List<String> lines) {
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
    
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Input file is missing.");
            return;
        }
        try {
            new Flatter().flatter(Paths.get(args[0]));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
