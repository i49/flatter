package io.github.i49.flatter.models;

public class Article extends Container {
    
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
        super.accept(visitor);
        visitor.leave(this);
    }
}
