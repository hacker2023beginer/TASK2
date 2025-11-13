package com.innowise.texttask.service.impl;

import com.innowise.texttask.component.TextComposite;
import com.innowise.texttask.component.TextLeaf;
import com.innowise.texttask.component.TypeTextComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TextSentenceWithEqualWordServiceTest {

    private TextSentenceWithEqualWordService service;

    @BeforeEach
    void setUp() {
        service = new TextSentenceWithEqualWordService();
    }

    @Test
    void testFindNumOfSentencesWithEqualWord_WithCommonWords() {
        TextComposite text = createTextWithSentences(
            createSentence("hello world"),
            createSentence("hello java"),
            createSentence("hello test")
        );

        int result = service.findNumOfSentencesWithEqualWord(text);
        assertEquals(3, result);
    }

    @Test
    void testFindNumOfSentencesWithEqualWord_WithPartialCommonWords() {
        TextComposite text = createTextWithSentences(
            createSentence("first sentence"),
            createSentence("second sentence"),
            createSentence("third word")
        );

        int result = service.findNumOfSentencesWithEqualWord(text);
        assertEquals(2, result);
    }

    @Test
    void testFindNumOfSentencesWithEqualWord_NoCommonWords() {
        TextComposite text = createTextWithSentences(
            createSentence("first word"),
            createSentence("second text"),
            createSentence("third line")
        );

        int result = service.findNumOfSentencesWithEqualWord(text);
        assertEquals(0, result);
    }

    @Test
    void testFindNumOfSentencesWithEqualWord_EmptyText() {
        TextComposite text = new TextComposite(TypeTextComponent.PARAGRAPH);
        int result = service.findNumOfSentencesWithEqualWord(text);
        assertEquals(0, result);
    }

    @Test
    void testFindNumOfSentencesWithEqualWord_SingleSentence() {
        TextComposite text = createTextWithSentences(createSentence("hello world"));
        int result = service.findNumOfSentencesWithEqualWord(text);
        assertEquals(0, result);
    }

    @Test
    void testFindNumOfSentencesWithEqualWord_MultipleCommonWords() {
        TextComposite text = createTextWithSentences(
            createSentence("hello world test"),
            createSentence("hello world java"),
            createSentence("hello python test")
        );

        int result = service.findNumOfSentencesWithEqualWord(text);
        assertEquals(3, result);
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
}

