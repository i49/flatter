package io.github.i49.flatter.models;

/**
 * The base interface for all document models.
 * 
 * @author i49
 */
public interface Block {
    
    /**
     * Accepts a visitor.
     * 
     * @param visitor the visitor who visits this block.
     */
    default void accept(Visitor visitor) {
    }
}
