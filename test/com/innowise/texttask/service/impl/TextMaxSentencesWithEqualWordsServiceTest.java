package com.innowise.texttask.service.impl;

import com.innowise.texttask.component.TextComposite;
import com.innowise.texttask.component.TextLeaf;
import com.innowise.texttask.component.TypeTextComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TextMaxSentencesWithEqualWordsServiceTest {

    private TextMaxSentencesWithEqualWordsService service;

    @BeforeEach
    void setUp() {
        service = new TextMaxSentencesWithEqualWordsService();
    }

    @Test
    void testFindMaxSentencesWithEqualWords_WithCommonWord() {
        TextComposite text = createTextWithSentences(
            createSentence("hello world"),
            createSentence("hello java"),
            createSentence("hello test")
        );

        int result = service.findMaxSentencesWithEqualWords(text);
        assertEquals(3, result);
    }

    @Test
    void testFindMaxSentencesWithEqualWords_WithDifferentWords() {
        TextComposite text = createTextWithSentences(
            createSentence("first sentence"),
            createSentence("second sentence"),
            createSentence("third sentence")
        );

        int result = service.findMaxSentencesWithEqualWords(text);
        assertEquals(3, result);
    }

    @Test
    void testFindMaxSentencesWithEqualWords_WithPartialCommonWords() {
        TextComposite text = createTextWithSentences(
            createSentence("hello world"),
            createSentence("hello java"),
            createSentence("test java"),
            createSentence("test python")
        );

        int result = service.findMaxSentencesWithEqualWords(text);
        assertEquals(3, result);
    }

    @Test
    void testFindMaxSentencesWithEqualWords_EmptyText() {
        TextComposite text = new TextComposite(TypeTextComponent.PARAGRAPH);
        int result = service.findMaxSentencesWithEqualWords(text);
        assertEquals(0, result);
    }

    @Test
    void testFindMaxSentencesWithEqualWords_SingleSentence() {
        TextComposite text = createTextWithSentences(createSentence("hello world"));
        int result = service.findMaxSentencesWithEqualWords(text);
        assertEquals(1, result);
    }

    private TextComposite createTextWithSentences(TextComposite... sentences) {
        TextComposite text = new TextComposite(TypeTextComponent.PARAGRAPH);
        for (TextComposite sentence : sentences) {
            text.add(sentence);
        }
        return text;
    }

    private TextComposite createSentence(String sentenceText) {
        TextComposite sentence = new TextComposite(TypeTextComponent.SENTENCE);
        String[] words = sentenceText.split("\\s+");
        
        for (String word : words) {
            TextComposite lexeme = new TextComposite(TypeTextComponent.LEXEME);
            TextComposite wordComposite = createWord(word);
            lexeme.add(wordComposite);
            sentence.add(lexeme);
        }
        
        return sentence;
    }

    private TextComposite createWord(String word) {
        TextComposite wordComposite = new TextComposite(TypeTextComponent.WORD);
        for (char c : word.toCharArray()) {
            TextLeaf symbol = new TextLeaf(String.valueOf(c), TypeTextComponent.SYMBOL);
            wordComposite.add(symbol);
        }
        return wordComposite;
    }
}

