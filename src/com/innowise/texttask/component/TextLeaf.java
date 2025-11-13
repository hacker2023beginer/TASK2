package com.innowise.texttask.component;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TextLeaf extends AbstractTextComponent {
    private static final Logger logger = LogManager.getLogger();
    private String text;

    public TextLeaf(String text) {
        logger.debug("Initializing TextLeaf with symbol: {}", text);
        this.text = text;
    }

    public TextLeaf(String text, TypeTextComponent type) {
        logger.debug("Creating TextLeaf. Type: {}, Text: '{}', Text length: {}",
                type, text, text.length());

        this.text = text;
        this.setTypeComponent(type);
    }

    @Override
    public String toString() {
        return text;
    }
}
