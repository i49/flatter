package io.github.i49.flatter.converter;

import java.util.ArrayList;
import java.util.List;

public class TextParser implements Parser {

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
}
