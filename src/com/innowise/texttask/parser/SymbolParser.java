package com.innowise.texttask.parser;

import com.innowise.texttask.component.TextComposite;
import com.innowise.texttask.component.TextLeaf;
import com.innowise.texttask.component.TypeTextComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SymbolParser extends BaseParser{
    private static final Logger logger = LogManager.getLogger();
    @Override
    public void parseComposite(String text, TextComposite parentComposite) {
        logger.info("Starting symbol parsing for text: '{}'", text);
        for (int i = 0; i < text.length(); i++) {
            char symbol = text.charAt(i);
            logger.debug("Get symbol: '{}'", String.valueOf(symbol));
            TextLeaf symbolLeaf = new TextLeaf(String.valueOf(symbol), TypeTextComponent.SYMBOL);
            logger.info("Created symbol leaf");
            parentComposite.addTextComponent(symbolLeaf);
            logger.info("Added symbol leaf to parent composite");
        }
    }
}
