package com.innowise.texttask.parser;

import com.innowise.texttask.component.TextComposite;

public abstract class BaseParser {
    protected BaseParser nextParser;
    public abstract void parseComposite(String text, TextComposite parentComposite);
}
