package com.innowise.texttask.service.impl;

import com.innowise.texttask.component.AbstractTextComponent;
import com.innowise.texttask.component.TextComposite;
import com.innowise.texttask.component.TextLeaf;
import com.innowise.texttask.component.TypeTextComponent;
import com.innowise.texttask.service.SentenceWithEqualWordService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class TextSentenceWithEqualWordService implements SentenceWithEqualWordService {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public int findNumOfSentencesWithEqualWord(TextComposite textComposite) {
        logger.info("Finding number of sentences with equal words");
        
        List<TextComposite> sentences = extractSentences(textComposite);

        Set<TextComposite> sentencesWithEqualWords = new HashSet<>();
        
        for (int i = 0; i < sentences.size(); i++) {
            TextComposite sentence1 = sentences.get(i);
            Set<String> words1 = extractWords(sentence1);
            
            for (int j = i + 1; j < sentences.size(); j++) {
                TextComposite sentence2 = sentences.get(j);
                Set<String> words2 = extractWords(sentence2);
                
                Set<String> commonWords = new HashSet<>(words1);
                commonWords.retainAll(words2);
                
                if (!commonWords.isEmpty()) {
                    sentencesWithEqualWords.add(sentence1);
                    sentencesWithEqualWords.add(sentence2);
                    logger.debug("Found common words between sentences: {}", commonWords);
                }
            }
        }

        int count = sentencesWithEqualWords.size();
        logger.info("Number of sentences with equal words: {}", count);
        return count;
    }

    private List<TextComposite> extractSentences(TextComposite textComposite) {
        List<TextComposite> sentences = new ArrayList<>();
        
        if (textComposite.getTypeComponent() == TypeTextComponent.SENTENCE) {
            sentences.add(textComposite);
            return sentences;
        }

        for (AbstractTextComponent component : textComposite.getTextComponent()) {
            if (component instanceof TextComposite) {
                TextComposite composite = (TextComposite) component;
                if (composite.getTypeComponent() == TypeTextComponent.SENTENCE) {
                    sentences.add(composite);
                } else {
                    sentences.addAll(extractSentences(composite));
                }
            }
        }
        
        return sentences;
    }

    private Set<String> extractWords(TextComposite sentence) {
        Set<String> words = new HashSet<>();
        
        for (AbstractTextComponent component : sentence.getTextComponent()) {
            if (component instanceof TextComposite) {
                TextComposite composite = (TextComposite) component;
                if (composite.getTypeComponent() == TypeTextComponent.LEXEME) {
                    words.addAll(extractWordsFromLexeme(composite));
                } else if (composite.getTypeComponent() == TypeTextComponent.WORD) {
                    String word = extractWordText(composite);
                    if (!word.isEmpty()) {
                        words.add(word.toLowerCase());
                    }
                } else {
                    words.addAll(extractWords(composite));
                }
            }
        }
        
        return words;
    }

    private Set<String> extractWordsFromLexeme(TextComposite lexeme) {
        Set<String> words = new HashSet<>();
        
        for (AbstractTextComponent component : lexeme.getTextComponent()) {
            if (component instanceof TextComposite) {
                TextComposite composite = (TextComposite) component;
                if (composite.getTypeComponent() == TypeTextComponent.WORD) {
                    String word = extractWordText(composite);
                    if (!word.isEmpty()) {
                        words.add(word.toLowerCase());
                    }
                } else {
                    words.addAll(extractWordsFromLexeme(composite));
                }
            }
        }
        
        return words;
    }

    private String extractWordText(TextComposite word) {
        StringBuilder wordText = new StringBuilder();
        
        for (AbstractTextComponent component : word.getTextComponent()) {
            if (component instanceof TextComposite) {
                wordText.append(extractWordText((TextComposite) component));
            } else if (component instanceof TextLeaf) {
                TextLeaf leaf = (TextLeaf) component;
                if (leaf.getTypeComponent() == TypeTextComponent.SYMBOL) {
                    wordText.append(leaf.toString());
                }
            }
        }
        
        return wordText.toString();
    }
}
