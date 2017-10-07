package io.github.i49.flatter.models;

/**
 * Visitor for models.
 * 
 * @author i49
 */
public interface Visitor {

    default void visit(Article article) {
    }
    
    default void visit(Blank blank) {
    }
    
    default void visit(Footer footer) {
    }

    default void visit(Heading heading) {
    }

    default void visit(Paragraph paragraph) {
    }
    
    default void visit(Preformatted pre) {
    }
    
    default void visit(Section section) {
    }
    
    default void leave(Article section) {
    }

    default void leave(Section section) {
    }
}
