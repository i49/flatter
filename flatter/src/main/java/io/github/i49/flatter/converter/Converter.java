package io.github.i49.flatter.converter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

import io.github.i49.flatter.models.Document;

public class Converter {
    
    private static final Logger log = Logger.getLogger(Converter.class.getName());
    
    private static final Charset CHARSET = Charset.forName("MS932");
    private static final String SUFFIX = "_out";
    
    private static final int MAX_LINE_LENGTH = 35;
    private static final String INDENT = "\u3000";

    private final Parser parser;
    private final Renderer renderder;
    
    public Converter() {
        this.parser = new TextParser();
        this.renderder = new TextRenderer(MAX_LINE_LENGTH, INDENT);
    }
    
    public void convert(Path input) throws IOException {
        Path output = getOutputPath(input);
        
        log.info("Input file: " + input.toAbsolutePath().toString());
        log.info("Output file: " + output.toAbsolutePath().toString());
        
        List<String> oldLines = Files.readAllLines(input, CHARSET);
        Document doc = parser.parse(oldLines);
        List<String> newLines = renderder.render(doc);
        Files.write(output, newLines, CHARSET);
    }
    
    private static Path getOutputPath(Path input) {
        String oldName = input.getFileName().toString();
        int lastIndex = oldName.lastIndexOf('.');
        StringBuilder b = new StringBuilder();
        if (lastIndex >= 0) {
            b.append(oldName.substring(0, lastIndex));
            b.append(SUFFIX);
            b.append(oldName.substring(lastIndex));
        } else {
            b.append(oldName).append(SUFFIX);
        }
        final String newName = b.toString();
        Path parent = input.getParent();
        if (parent != null) {
            return parent.resolve(newName);
        } else {
            return Paths.get(newName);
        }
    }
    
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Input file is missing.");
            return;
        }
        try {
            new Converter().convert(Paths.get(args[0]));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
