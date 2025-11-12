package com.innowise.texttask.parser;

import com.innowise.texttask.component.TextComposite;
import com.innowise.texttask.component.TypeTextComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ParagraphParser extends BaseParser{
    private static final Logger logger = LogManager.getLogger();
    private static final String PARAGRAPH_DELIMITER = "\\R+";

    public ParagraphParser(BaseParser nextParser) {
        logger.debug("Next parser is {} parser", nextParser.getClass().getSimpleName());
        this.nextParser = nextParser;
    }

    @Override
    public void parseComposite(String text, TextComposite parentComposite) {
        logger.info("Starting paragraph parsing for text: '{}'", text);
        String[] paragraphs = text.split(PARAGRAPH_DELIMITER);
        logger.debug("Split into '{}' paragraphs", paragraphs.length);
        for (String paragraph : paragraphs) {
            logger.debug("Processing paragraph: '{}'", paragraph);
            TextComposite paragraphComposite = new TextComposite(TypeTextComponent.PARAGRAPH);
            parentComposite.addTextComponent(paragraphComposite);
            logger.info("Paragraph composite created and added to parent");
            logger.info("Delegated paragraph '{}' to next parser", paragraph);
            nextParser.parseComposite(paragraph, paragraphComposite);
        }
    }
}
