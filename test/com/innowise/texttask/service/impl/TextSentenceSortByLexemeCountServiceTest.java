package com.innowise.texttask.service.impl;

import com.innowise.texttask.component.TextComposite;
import com.innowise.texttask.component.TextLeaf;
import com.innowise.texttask.component.TypeTextComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TextSentenceSortByLexemeCountServiceTest {

    private TextSentenceSortByLexemeCountService service;

    @BeforeEach
    void setUp() {
        service = new TextSentenceSortByLexemeCountService();
    }

    @Test
    void testSortSentencesByLexemeCount_AscendingOrder() {
        // Создаем предложения с разным количеством лексем
        TextComposite text = createTextWithSentences(
            createSentence("one two three"),      // 3 лексемы
            createSentence("one"),                 // 1 лексема
            createSentence("one two three four")  // 4 лексемы
        );

        List<TextComposite> result = service.sortSentencesByLexemeCount(text);
        assertAll(
            () -> assertEquals(3, result.size()),
            () -> assertEquals(1, countLexemes(result.get(0))), // "one"
            () ->assertEquals(3, countLexemes(result.get(1))), // "one two three"
            () ->assertEquals(4, countLexemes(result.get(2))) // "one two three four"
        );
    }

    @Test
    void testSortSentencesByLexemeCount_SameCount() {
        // Создаем предложения с одинаковым количеством лексем
        TextComposite text = createTextWithSentences(
            createSentence("one two"),
            createSentence("three four")
        );

        List<TextComposite> result = service.sortSentencesByLexemeCount(text);

        assertAll(
            () -> assertEquals(2, result.size()),
            () -> assertEquals(2, countLexemes(result.get(0))),
            () -> assertEquals(2, countLexemes(result.get(1)))
        );
    }

    @Test
    void testSortSentencesByLexemeCount_EmptyText() {
        TextComposite text = new TextComposite(TypeTextComponent.PARAGRAPH);
        List<TextComposite> result = service.sortSentencesByLexemeCount(text);
        assertTrue(result.isEmpty());
    }

    @Test
    void testSortSentencesByLexemeCount_SingleSentence() {
        TextComposite text = createTextWithSentences(createSentence("hello world"));
        List<TextComposite> result = service.sortSentencesByLexemeCount(text);

        assertAll(
            () -> assertEquals(1, result.size()),
            () -> assertEquals(2, countLexemes(result.get(0)))
        );
    }

    @Test
    void testSortSentencesByLexemeCount_ComplexText() {
        TextComposite text = createTextWithSentences(
            createSentence("a"),                           // 1
            createSentence("a b c d e"),                  // 5
            createSentence("a b"),                         // 2
            createSentence("a b c")                        // 3
        );

        List<TextComposite> result = service.sortSentencesByLexemeCount(text);

        assertAll(
            () -> assertEquals(4, result.size()),
            () -> assertEquals(1, countLexemes(result.get(0))),
            () -> assertEquals(2, countLexemes(result.get(1))),
            () -> assertEquals(3, countLexemes(result.get(2))),
            () -> assertEquals(5, countLexemes(result.get(3)))
        );
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

    private int countLexemes(TextComposite sentence) {
        int count = 0;
        for (com.innowise.texttask.component.AbstractTextComponent component : sentence.getChildren()) {
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
}

