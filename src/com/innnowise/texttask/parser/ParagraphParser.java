package com.innnowise.texttask.parser;

import com.innnowise.texttask.component.TextComposite;
import com.innnowise.texttask.component.TypeTextComponent;

public class ParagraphParser extends BaseParser{
    private static final String PARAGRAPH_DELIMITER = "\\R+";

    public ParagraphParser(BaseParser nextParser) {
        this.nextParser = nextParser;
    }

    @Override
    public void parseComposite(String text, TextComposite parentComposite) {
        String[] paragraphs = text.split(PARAGRAPH_DELIMITER);
        for (String paragraph : paragraphs) {
            TextComposite paragraphComposite = new TextComposite(TypeTextComponent.PARAGRAPH);
            parentComposite.addTextComponent(paragraphComposite);
            nextParser.parseComposite(paragraph, paragraphComposite);
        }
    }
}
