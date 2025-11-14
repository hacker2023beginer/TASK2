package com.innowise.texttask.service.impl;

import com.innowise.texttask.component.AbstractTextComponent;
import com.innowise.texttask.component.TextComposite;
import com.innowise.texttask.component.TypeTextComponent;
import com.innowise.texttask.service.SentenceSortByLexemeCountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TextSentenceSortByLexemeCountService implements SentenceSortByLexemeCountService {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public List<TextComposite> sortSentencesByLexemeCount(TextComposite textComposite) {
        logger.info("Sorting sentences by lexeme count");
        
        List<TextComposite> sentences = extractSentences(textComposite);
        logger.debug("Found {} sentences", sentences.size());
        
        sentences.sort(Comparator.comparingInt(this::countLexemes));
        
        logger.info("Sentences sorted by lexeme count");
        return sentences;
    }

    private List<TextComposite> extractSentences(TextComposite textComposite) {
        List<TextComposite> sentences = new ArrayList<>();
        
        if (textComposite.getTypeComponent() == TypeTextComponent.SENTENCE) {
            sentences.add(textComposite);
            return sentences;
        }

        for (AbstractTextComponent component : textComposite.getChildren()) {
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

    private int countLexemes(TextComposite sentence) {
        int count = 0;
        
        for (AbstractTextComponent component : sentence.getChildren()) {
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

