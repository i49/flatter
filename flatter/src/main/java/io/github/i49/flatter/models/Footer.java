package io.github.i49.flatter.models;

public class Footer implements Block {
    
    private final String text;
    
    public Footer(String text) {
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
