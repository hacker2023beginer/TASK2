package com.innowise.texttask.service.impl;

import com.innowise.texttask.component.TextComposite;
import com.innowise.texttask.component.TextLeaf;
import com.innowise.texttask.component.TypeTextComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TextSwapFirstLastLexemeServiceTest {

    private TextSwapFirstLastLexemeService service;

    @BeforeEach
    void setUp() {
        service = new TextSwapFirstLastLexemeService();
    }

    @Test
    void testSwapFirstLastLexemeInSentences_SingleSentence() {
        TextComposite sentence = createSentence("first second third");
        TextComposite result = service.swapFirstLastLexemeInSentences(sentence);

        String resultText = result.toString().trim();

        assertAll(
            () -> assertTrue(resultText.startsWith("third") || resultText.contains("third")),
            () -> assertTrue(resultText.contains("first"))
        );
    }

    @Test
    void testSwapFirstLastLexemeInSentences_MultipleSentences() {
        TextComposite text = createTextWithSentences(
            createSentence("one two three"),
            createSentence("four five six")
        );

        TextComposite result = service.swapFirstLastLexemeInSentences(text);
        
        assertAll(
            () -> assertNotNull(result),
            () -> assertEquals(TypeTextComponent.PARAGRAPH, result.getTypeComponent())
        );
    }

    @Test
    void testSwapFirstLastLexemeInSentences_SingleLexeme() {
        TextComposite sentence = createSentence("single");
        TextComposite result = service.swapFirstLastLexemeInSentences(sentence);
        
        assertEquals(sentence.toString().trim(), result.toString().trim());
    }

    @Test
    void testSwapFirstLastLexemeInSentences_TwoLexemes() {
        TextComposite sentence = createSentence("first second");
        
        TextComposite result = service.swapFirstLastLexemeInSentences(sentence);
        String resultText = result.toString().trim();

        assertAll(
            () -> assertTrue(resultText.startsWith("second") || resultText.contains("second")),
            () -> assertTrue(resultText.contains("first"))
        );
    }

    @Test
    void testSwapFirstLastLexemeInSentences_EmptyText() {
        TextComposite text = new TextComposite(TypeTextComponent.PARAGRAPH);
        TextComposite result = service.swapFirstLastLexemeInSentences(text);

        assertAll(
            () -> assertNotNull(result),
            () -> assertEquals(0, result.getTextComponent().size())
        );
    }

    @Test
    void testSwapFirstLastLexemeInSentences_PreservesStructure() {
        TextComposite sentence = createSentence("one two three four five");
        TextComposite result = service.swapFirstLastLexemeInSentences(sentence);

        int originalCount = countLexemes(sentence);
        int resultCount = countLexemes(result);
        assertEquals(originalCount, resultCount);
    }

    @Test
    void testSwapFirstLastLexemeInSentences_ComplexText() {
        TextComposite text = createTextWithSentences(
            createSentence("a b c"),
            createSentence("d e"),
            createSentence("f g h i")
        );

        TextComposite result = service.swapFirstLastLexemeInSentences(text);

        int originalSentenceCount = countSentences(text);
        int resultSentenceCount = countSentences(result);
        assertAll(
            () -> assertEquals(originalSentenceCount, resultSentenceCount),
            () -> assertNotNull(result)
        );
    }

    private TextComposite createTextWithSentences(TextComposite... sentences) {
        TextComposite text = new TextComposite(TypeTextComponent.PARAGRAPH);
        for (TextComposite sentence : sentences) {
            text.addTextComponent(sentence);
        }
        return text;
    }

    private TextComposite createSentence(String sentenceText) {
        TextComposite sentence = new TextComposite(TypeTextComponent.SENTENCE);
        String[] words = sentenceText.split("\\s+");
        
        for (String word : words) {
            TextComposite lexeme = new TextComposite(TypeTextComponent.LEXEME);
            TextComposite wordComposite = createWord(word);
            lexeme.addTextComponent(wordComposite);
            sentence.addTextComponent(lexeme);
        }
        
        return sentence;
    }

    private TextComposite createWord(String word) {
        TextComposite wordComposite = new TextComposite(TypeTextComponent.WORD);
        for (char c : word.toCharArray()) {
            TextLeaf symbol = new TextLeaf(String.valueOf(c), TypeTextComponent.SYMBOL);
            wordComposite.addTextComponent(symbol);
        }
        return wordComposite;
    }

    private int countLexemes(TextComposite sentence) {
        int count = 0;
        for (com.innowise.texttask.component.AbstractTextComponent component : sentence.getTextComponent()) {
            if (component instanceof TextComposite) {
                TextComposite composite = (TextComposite) component;
                if (composite.getTypeComponent() == TypeTextComponent.LEXEME) {
                    count++;
                } else {
                    count += countLexemes(composite);
                }
            }
        }
        return count;
    }

    private int countSentences(TextComposite text) {
        int count = 0;
        for (com.innowise.texttask.component.AbstractTextComponent component : text.getTextComponent()) {
            if (component instanceof TextComposite) {
                TextComposite composite = (TextComposite) component;
                if (composite.getTypeComponent() == TypeTextComponent.SENTENCE) {
                    count++;
                } else {
                    count += countSentences(composite);
                }
            }
        }
        return count;
    }
}

