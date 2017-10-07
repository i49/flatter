package io.github.i49.flatter.models;

/**
 * Blank line.
 * 
 * @author i49
 */
public class Blank implements Block {
    
    /**
     * The only instance of this class.
     */
    public static final Blank INSTANCE = new Blank();
    
    private Blank() {
    }
    
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
    
    @Override
    public String toString() {
        return "";
    }
}
