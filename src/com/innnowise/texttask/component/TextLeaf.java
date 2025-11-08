package com.innnowise.texttask.component;

public class TextLeaf extends TextComponent{
    char symbol;

    public TextLeaf(char symbol) {
        this.symbol = symbol;
    }
    @Override
    public String toString() {
        return String.valueOf(symbol);
    }
}
