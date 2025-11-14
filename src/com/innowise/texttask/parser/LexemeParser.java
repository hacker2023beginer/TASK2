package com.innowise.texttask.parser;

import com.innowise.texttask.component.TextComposite;
import com.innowise.texttask.component.TypeTextComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LexemeParser extends BaseParser {
    private static final Logger logger = LogManager.getLogger();
    private static final String LEXEME_DELIMITER = "\\s+";

    public LexemeParser(BaseParser nextParser) {
        logger.debug("Next parser is {} parser", nextParser.getClass().getSimpleName());
        this.nextParser = nextParser;
    }

    @Override
    public void parseComposite(String text, TextComposite parentComposite) {
        logger.info("Starting lexeme parsing for text: '{}'", text);
        String[] lexemes = text.split(LEXEME_DELIMITER);
        logger.debug("Split into {} lexemes", lexemes.length);
        for (String lexeme : lexemes) {
            logger.debug("Processing lexeme: '{}'", lexeme);
            TextComposite lexemeComposite = new TextComposite(TypeTextComponent.LEXEME);
            parentComposite.add(lexemeComposite);
            logger.info("Lexeme composite created and added to parent");
            logger.info("Delegated lexeme '{}' to next parser", lexeme);
            nextParser.parseComposite(lexeme, lexemeComposite);
        }
        logger.info("Finished lexeme parsing for text");
    }

}
