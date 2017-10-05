package io.github.i49.flatter.converter;

import java.util.List;

public interface Parser {

    List<Block> parse(List<String> lines);
}
