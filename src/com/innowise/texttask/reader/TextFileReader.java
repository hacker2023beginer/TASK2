package com.innowise.texttask.reader;

import com.innowise.texttask.exception.CustomTextException;

import java.nio.file.Path;
import java.util.List;

public interface TextFileReader {
    List<String> readFromFile(Path filePath) throws CustomTextException;
}
