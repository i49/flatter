package io.github.i49.flatter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Block {
    
    public static final Block BLANK = new Block(BlockType.BLANK);

    private BlockType type;
    private final List<String> content = new ArrayList<>();
    
    public Block() {
        this.type = BlockType.PARAGRAPH;
    }
    
    public Block(BlockType type) {
        this.type = type;
    }
    
    public Block(BlockType type, String content) {
        this.type = type;
        this.content.add(content);
    }

    public BlockType getType() {
        return type;
    }
    
    public Block setType(BlockType type) {
        this.type = type;
        return this;
    }
    
    public String getContent() {
        return this.content.stream()
            .map(Lines::trim)
            .collect(Collectors.joining());
    }
    
    public char getFirstChar() {
        String line = Lines.trimLeft(this.content.get(0));
        return line.charAt(0);
    }
    
    public Block append(String content) {
        this.content.add(content);
        return this;
    }
}
