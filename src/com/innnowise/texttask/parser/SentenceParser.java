package com.innnowise.texttask.parser;

import com.innnowise.texttask.component.TextComposite;
import com.innnowise.texttask.component.TypeTextComponent;

public class SentenceParser extends BaseParser {
    private static final String SENTENCE_DELIMITER = "(?<=[.!?])\\s+";

    public SentenceParser(BaseParser nextParser) {
        this.nextParser = nextParser;
    }

    @Override
    public void parseComposite(String text, TextComposite parentComposite) {
        String[] sentences = text.split(SENTENCE_DELIMITER);
        for (String sentence : sentences) {
            TextComposite sentenceComposite = new TextComposite(TypeTextComponent.SENTENCE);
            parentComposite.addTextComponent(sentenceComposite);
            nextParser.parseComposite(sentence, sentenceComposite);
        }
    }
}
