package com.innowise.texttask.reader.impl;

import com.innowise.texttask.exception.CustomTextException;
import com.innowise.texttask.reader.TextFileReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class TextFileReaderImpl implements TextFileReader {
    @Override
    public List<String> readFromFile(Path filePath) throws CustomTextException {
        if (filePath == null || Files.notExists(filePath)) {
            throw new CustomTextException("File not found");
        }
        if (Files.isDirectory(filePath)) {
            throw new CustomTextException("Path points to a directory, not a file");
        }
        try {
            return Files.readAllLines(filePath);
        } catch (IOException e) {
            throw new CustomTextException("Error reading file " + e);
        }
    }
}

