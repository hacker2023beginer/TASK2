package com.innowise.texttask.service.impl;

import com.innowise.texttask.component.AbstractTextComponent;
import com.innowise.texttask.component.TextComposite;
import com.innowise.texttask.component.TextLeaf;
import com.innowise.texttask.component.TypeTextComponent;
import com.innowise.texttask.service.MaxSentencesWithEqualWordsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class TextMaxSentencesWithEqualWordsService implements MaxSentencesWithEqualWordsService {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public int findMaxSentencesWithEqualWords(TextComposite textComposite) {
        logger.info("Finding maximum number of sentences with equal words");
        
        List<TextComposite> sentences = extractSentences(textComposite);

        Map<String, Set<TextComposite>> wordToSentences = new HashMap<>();
        
        for (TextComposite sentence : sentences) {
            Set<String> wordsInSentence = extractWords(sentence);
            for (String word : wordsInSentence) {
                wordToSentences.computeIfAbsent(word.toLowerCase(), k -> new HashSet<>()).add(sentence);
            }
        }

        int maxCount = 0;
        for (Map.Entry<String, Set<TextComposite>> entry : wordToSentences.entrySet()) {
            int sentenceCount = entry.getValue().size();
            if (sentenceCount > maxCount) {
                maxCount = sentenceCount;
                logger.debug("Found word '{}' in {} sentences", entry.getKey(), sentenceCount);
            }
        }

        logger.info("Maximum number of sentences with equal words: {}", maxCount);
        return maxCount;
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
                        words.add(word);
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
                        words.add(word);
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

