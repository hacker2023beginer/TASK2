package com.innowise.texttask.parser;

import com.innowise.texttask.component.TextComposite;
import com.innowise.texttask.component.TypeTextComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WordParser extends BaseParser {
    private static final Logger logger = LogManager.getLogger();
    private static final String WORD_DELIMITER = "[^a-zA-Z]+";

    public WordParser(BaseParser nextParser) {
        logger.debug("Next parser is {} parser", nextParser.getClass().getSimpleName());
        this.nextParser = nextParser;
    }

    @Override
    public void parseComposite(String text, TextComposite parentComposite) {
        logger.info("Starting word parsing for text: '{}'", text);
        String[] words = text.split(WORD_DELIMITER);
        logger.debug("Split into '{}' words", words.length);
        for (String word : words) {
            logger.debug("Processing word: '{}'", word);
            TextComposite wordComposite = new TextComposite(TypeTextComponent.WORD);
            parentComposite.addTextComponent(wordComposite);
            logger.info("Word composite created and added to parent");
            logger.info("Delegated word '{}' to next parser", word);
            nextParser.parseComposite(word, wordComposite);
        }
    }
}
