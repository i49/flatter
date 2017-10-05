package io.github.i49.flatter.converter;

import java.util.List;

public interface Renderer {

    List<String> render(List<Block> blocks);
}
