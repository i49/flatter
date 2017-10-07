package io.github.i49.flatter.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Block which can contain other blocks.
 * 
 * @author i49
 */
public class Container implements Block {

    private final List<Block> children = new ArrayList<>();
    
    public boolean isEmpty() {
        return children.isEmpty();
    }
    
    public void appendChild(Block child) {
        if (child == null) {
            throw new NullPointerException();
        }
        this.children.add(child);
    }
    
    @Override
    public void accept(Visitor visitor) {
        children.forEach(b->b.accept(visitor));
    }
    
    @Override
    public String toString() {
        return children.toString();
    }
}
