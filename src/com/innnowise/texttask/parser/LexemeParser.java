package com.innnowise.texttask.parser;

import com.innnowise.texttask.component.TextComposite;
import com.innnowise.texttask.component.TypeTextComponent;

public class LexemeParser extends BaseParser {
    private static final String LEXEME_DELIMITER = "\\s+";

    public LexemeParser(BaseParser nextParser) {
        this.nextParser = nextParser;
    }

    @Override
    public void parseComposite(String text, TextComposite parentComposite) {
        String[] lexemes = text.split(LEXEME_DELIMITER);
        for (String lexeme : lexemes) {
            TextComposite lexemeComposite = new TextComposite(TypeTextComponent.LEXEME);
            parentComposite.addTextComponent(lexemeComposite);
            nextParser.parseComposite(lexeme, lexemeComposite);
        }
    }
}
