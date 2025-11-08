package com.innnowise.texttask.parser;

import com.innnowise.texttask.component.TextComposite;
import com.innnowise.texttask.component.TextLeaf;

public class SymbolParser extends BaseParser{

    @Override
    public void parseComposite(String text, TextComposite parentComposite) {
        for (int i = 0; i < text.length(); i++) {
            char symbol = text.charAt(i);
            TextLeaf symbolLeaf = new TextLeaf(symbol);
            parentComposite.addTextComponent(symbolLeaf);
        }
    }
}
