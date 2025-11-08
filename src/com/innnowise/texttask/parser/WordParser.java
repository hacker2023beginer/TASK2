package com.innnowise.texttask.parser;

import com.innnowise.texttask.component.TextComposite;
import com.innnowise.texttask.component.TypeTextComponent;

public class WordParser extends BaseParser {
    private static final String WORD_DELIMITER = "[^a-zA-Z]+";

    public WordParser(BaseParser nextParser) {
        this.nextParser = nextParser;
    }

    @Override
    public void parseComposite(String text, TextComposite parentComposite) {
        String[] words = text.split(WORD_DELIMITER);
        for (String word : words) {
            TextComposite wordComposite = new TextComposite(TypeTextComponent.WORD);
            parentComposite.addTextComponent(wordComposite);
            nextParser.parseComposite(word, wordComposite);
        }
    }
}
