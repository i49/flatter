package io.github.i49.flatter.models;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.github.i49.flatter.base.Lines;

public class Paragraph implements Block {

    private final List<String> content = new ArrayList<>();
    
    public void append(String text) {
        this.content.add(text);
    }
    
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
    
    @Override
    public String toString() {
        return this.content.stream()
                .map(Lines::trim)
                .collect(Collectors.joining());
    }
}
