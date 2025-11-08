package com.innnowise.texttask.parser;

import com.innnowise.texttask.component.TextComposite;

public abstract class BaseParser {
    protected BaseParser nextParser;
    public abstract void parseComposite(String text, TextComposite parentComposite);
}
