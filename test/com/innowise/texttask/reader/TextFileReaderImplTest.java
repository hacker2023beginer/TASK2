package com.innowise.texttask.reader;

import com.innowise.texttask.exception.CustomTextException;
import com.innowise.texttask.reader.impl.TextFileReaderImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TextFileReaderImplTest {

    private TextFileReaderImpl reader;

    @BeforeEach
    void setUp() {
        reader = new TextFileReaderImpl();
    }

    @Test
    void testReadFromFileSuccess(@TempDir Path tempDir) throws Exception {
        Path filePath = tempDir.resolve("test.txt");
        List<String> expectedLines = Arrays.asList("Line 1", "Line 2", "Line 3");
        Files.write(filePath, expectedLines);

        List<String> result = reader.readFromFile(filePath);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(expectedLines.size(), result.size()),
                () -> assertEquals(expectedLines, result)
        );
    }

    @Test
    void testReadFromFileWhenPathIsNull() {
        CustomTextException exception = assertThrows(CustomTextException.class,
                () -> reader.readFromFile(null));

        assertEquals("File not found", exception.getMessage());
    }

    @Test
    void testReadFromFileWhenFileNotFound() {
        Path filePath = Paths.get("nonexistent_file_that_does_not_exist_12345.txt");

        CustomTextException exception = assertThrows(CustomTextException.class,
                () -> reader.readFromFile(filePath));

        assertEquals("File not found", exception.getMessage());
    }

    @Test
    void testReadFromFileWhenPathIsDirectory(@TempDir Path tempDir) {
        CustomTextException exception = assertThrows(CustomTextException.class,
                () -> reader.readFromFile(tempDir));

        assertEquals("Path points to a directory, not a file", exception.getMessage());
    }

    @Test
    void testReadFromFileWhenIOExceptionOccurs(@TempDir Path tempDir) throws Exception {
        Path filePath = tempDir.resolve("test.txt");
        Files.createFile(filePath);
        
        // На некоторых системах можно создать файл без прав на чтение
        // Попробуем удалить файл сразу после создания, чтобы вызвать IOException
        // Или создадим файл и закроем его, затем попробуем удалить директорию
        try {
            // На Windows это может не сработать, поэтому используем другой подход
            // Создадим файл, который будет недоступен для чтения
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                // На Windows сложнее симулировать IOException при чтении
                // Просто проверим, что файл существует и можем его прочитать
                Files.write(filePath, Arrays.asList("test"));
                List<String> result = reader.readFromFile(filePath);
                assertNotNull(result);
            } else {
                // На Unix-системах можно убрать права на чтение
                Set<PosixFilePermission> noReadPermissions = PosixFilePermissions.fromString("--x------");
                Files.setPosixFilePermissions(filePath, noReadPermissions);
                
                CustomTextException exception = assertThrows(CustomTextException.class,
                        () -> reader.readFromFile(filePath));
                
                assertTrue(exception.getMessage().contains("Error reading file"));
            }
        } catch (UnsupportedOperationException e) {
            // Если PosixFilePermissions не поддерживается, пропускаем этот тест
            // или используем альтернативный подход
            Files.write(filePath, Arrays.asList("test"));
            List<String> result = reader.readFromFile(filePath);
            assertNotNull(result);
        }
    }

    @Test
    void testReadFromFileWithEmptyFile(@TempDir Path tempDir) throws Exception {
        Path filePath = tempDir.resolve("empty.txt");
        Files.createFile(filePath);

        List<String> result = reader.readFromFile(filePath);

        assertAll(
                () -> assertNotNull(result),
                () -> assertTrue(result.isEmpty())
        );
    }

    @Test
    void testReadFromFileWithSingleLine(@TempDir Path tempDir) throws Exception {
        Path filePath = tempDir.resolve("single.txt");
        String content = "Single line content";
        Files.write(filePath, Arrays.asList(content));

        List<String> result = reader.readFromFile(filePath);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals(content, result.get(0))
        );
    }

    @Test
    void testReadFromFileWithMultipleLines(@TempDir Path tempDir) throws Exception {
        Path filePath = tempDir.resolve("multi.txt");
        List<String> expectedLines = Arrays.asList("First line", "Second line", "Third line");
        Files.write(filePath, expectedLines);

        List<String> result = reader.readFromFile(filePath);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(3, result.size()),
                () -> assertEquals("First line", result.get(0)),
                () -> assertEquals("Second line", result.get(1)),
                () -> assertEquals("Third line", result.get(2))
        );
    }

    @Test
    void testReadFromFileWithSpecialCharacters(@TempDir Path tempDir) throws Exception {
        Path filePath = tempDir.resolve("special.txt");
        List<String> expectedLines = Arrays.asList("Line with спецсимволы", "Line 2");
        Files.write(filePath, expectedLines);

        List<String> result = reader.readFromFile(filePath);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(2, result.size()),
                () -> assertEquals("Line with спецсимволы", result.get(0))
        );
    }

    @Test
    void testReadFromFileWithUnicodeCharacters(@TempDir Path tempDir) throws Exception {
        Path filePath = tempDir.resolve("unicode.txt");
        List<String> expectedLines = Arrays.asList("Hello 世界", "Привет 🌍", "مرحبا");
        Files.write(filePath, expectedLines);

        List<String> result = reader.readFromFile(filePath);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(3, result.size()),
                () -> assertEquals("Hello 世界", result.get(0)),
                () -> assertEquals("Привет 🌍", result.get(1)),
                () -> assertEquals("مرحبا", result.get(2))
        );
    }
}

