package io.github.i49.flatter.converter;

import java.util.List;

import io.github.i49.flatter.models.Document;

public interface Parser {

//    List<Block> parse(List<String> lines);
    
    Document parse(List<String> lines);
}
