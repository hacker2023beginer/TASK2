package com.innowise.texttask.service.impl;

import com.innowise.texttask.component.AbstractTextComponent;
import com.innowise.texttask.component.TextComposite;
import com.innowise.texttask.component.TypeTextComponent;
import com.innowise.texttask.service.SwapFirstLastLexemeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class TextSwapFirstLastLexemeService implements SwapFirstLastLexemeService {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public TextComposite swapFirstLastLexemeInSentences(TextComposite textComposite) {
        logger.info("Swapping first and last lexemes in all sentences");
        
        TextComposite result = new TextComposite(textComposite.getTypeComponent());
        
        if (textComposite.getTypeComponent() == TypeTextComponent.SENTENCE) {
            return swapLexemesInSentence(textComposite);
        }

        for (AbstractTextComponent component : textComposite.getChildren()) {
            if (component instanceof TextComposite) {
                TextComposite composite = (TextComposite) component;
                if (composite.getTypeComponent() == TypeTextComponent.SENTENCE) {
                    result.add(swapLexemesInSentence(composite));
                } else {
                    result.add(swapFirstLastLexemeInSentences(composite));
                }
            } else {
                result.add(component);
            }
        }
        
        logger.info("First and last lexemes swapped in all sentences");
        return result;
    }

    private TextComposite swapLexemesInSentence(TextComposite sentence) {
        logger.debug("Swapping lexemes in sentence");
        
        List<AbstractTextComponent> components = sentence.getChildren();
        
        int firstLexemeIndex = -1;
        int lastLexemeIndex = -1;
        
        for (int i = 0; i < components.size(); i++) {
            AbstractTextComponent component = components.get(i);
            if (component instanceof TextComposite) {
                TextComposite composite = (TextComposite) component;
                if (composite.getTypeComponent() == TypeTextComponent.LEXEME) {
                    if (firstLexemeIndex == -1) {
                        firstLexemeIndex = i;
                    }
                    lastLexemeIndex = i;
                }
            }
        }
        
        if (firstLexemeIndex == -1 || lastLexemeIndex == -1 || firstLexemeIndex == lastLexemeIndex) {
            logger.debug("Sentence has less than 2 lexemes, no swap needed");
            return sentence;
        }
        
        TextComposite result = new TextComposite(TypeTextComponent.SENTENCE);
        
        for (int i = 0; i < components.size(); i++) {
            AbstractTextComponent component = components.get(i);
            
            if (i == firstLexemeIndex) {
                TextComposite lastLexeme = (TextComposite) components.get(lastLexemeIndex);
                result.add(lastLexeme);
            } else if (i == lastLexemeIndex) {
                TextComposite firstLexeme = (TextComposite) components.get(firstLexemeIndex);
                result.add(firstLexeme);
            } else if (component instanceof TextComposite) {
                TextComposite composite = (TextComposite) component;
                if (composite.getTypeComponent() != TypeTextComponent.LEXEME) {
                    result.add(swapFirstLastLexemeInSentences(composite));
                } else {
                    result.add(component);
                }
            } else {
                result.add(component);
            }
        }
        
        logger.debug("Lexemes swapped in sentence");
        return result;
    }
}

