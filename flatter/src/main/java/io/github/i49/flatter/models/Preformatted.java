package io.github.i49.flatter.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Pre-formatted document block.
 * 
 * @author i49
 */
public class Preformatted implements Block, Iterable<String> {

    private final List<String> lines = new ArrayList<>();
    
    public void append(String line) {
        this.lines.add(line);
    }
    
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
    
    @Override
    public Iterator<String> iterator() {
        return lines.iterator();
    }
    
    @Override
    public String toString() {
        return lines.stream().collect(Collectors.joining("\n"));
    }
}

