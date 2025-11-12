package com.innowise.texttask.parser;

import com.innowise.texttask.component.TextComposite;
import com.innowise.texttask.component.TypeTextComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SentenceParser extends BaseParser {
    private static final Logger logger = LogManager.getLogger();
    private static final String SENTENCE_DELIMITER = "(?<=[.!?])\\s+";

    public SentenceParser(BaseParser nextParser) {
        logger.debug("Next parser is {} parser", nextParser.getClass().getSimpleName());
        this.nextParser = nextParser;
    }

    @Override
    public void parseComposite(String text, TextComposite parentComposite) {
        logger.info("Starting sentence parsing for text: '{}'", text);
        String[] sentences = text.split(SENTENCE_DELIMITER);
        logger.debug("Split into '{}' sentences", sentences.length);
        for (String sentence : sentences) {
            logger.debug("Processing sentence: '{}'", sentence);
            TextComposite sentenceComposite = new TextComposite(TypeTextComponent.SENTENCE);
            parentComposite.addTextComponent(sentenceComposite);
            logger.info("Sentence composite created and added to parent");
            logger.info("Delegated sentence '{}' to next parser", sentence);
            nextParser.parseComposite(sentence, sentenceComposite);
        }
    }
}
