package io.github.i49.flatter.converter;

import java.util.List;

import io.github.i49.flatter.models.Document;

public interface Renderer {

    List<String> render(Document doc);
}
