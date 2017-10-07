package io.github.i49.flatter.models;

/**
 * Heading of a section or an article.
 * 
 * @author i49
 */
public class Heading implements Block {

    private final String text;
    
    public Heading(String text) {
        this.text = text;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
    
    @Override
    public String toString() {
        return text;
    }
}
