package com.innowise.texttask.component;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TextLeaf extends TextComponent{
    private static final Logger logger = LogManager.getLogger();
    private char symbol;

    public TextLeaf(char symbol) {
        logger.debug("Initializing TextLeaf with symbol: {}", String.valueOf(symbol));
        this.symbol = symbol;
    }
    @Override
    public String toString() {
        return String.valueOf(symbol);
    }
}
