package com.innnowise.texttask.parser;

import com.innnowise.texttask.component.TextComposite;
import com.innnowise.texttask.component.TypeTextComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ParagraphParser extends BaseParser{
    private static final Logger logger = LogManager.getLogger(ParagraphParser.class);
    private static final String PARAGRAPH_DELIMITER = "\\R+";

    public ParagraphParser(BaseParser nextParser) {
        this.nextParser = nextParser;
    }

    @Override
    public void parseComposite(String text, TextComposite parentComposite) {
        logger.info("Starting paragraph parsing for text: '{}'", text);
        String[] paragraphs = text.split(PARAGRAPH_DELIMITER);
        logger.debug("Split into '{}' paragraphs", text);
        for (String paragraph : paragraphs) {
            TextComposite paragraphComposite = new TextComposite(TypeTextComponent.PARAGRAPH);
            parentComposite.addTextComponent(paragraphComposite);
            nextParser.parseComposite(paragraph, paragraphComposite);
        }
    }
}
