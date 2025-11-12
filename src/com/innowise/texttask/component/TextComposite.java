package com.innowise.texttask.component;

import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TextComposite extends TextComponent{
    private static final Logger logger = LogManager.getLogger();
    private static final String PARAGRAPH_SIGN = "\n";
    private static final String SPACE_SIGN = " ";
    private final ArrayList<TextComponent> textComponent = new ArrayList<>();

    public TextComposite(TypeTextComponent type) {
        logger.debug("Creating TextComposite with type {}", type);
        this.type = type;
    }

    public ArrayList<TextComponent> getTextComponent() {
        ArrayList<TextComponent> copyArrayOfTextComponent = new ArrayList<>(textComponent);
        return copyArrayOfTextComponent;
    }

    public void addTextComponent(TextComponent textComponent){
        logger.debug("Add TextComponent with type {}", textComponent.getTypeComponent());
        this.textComponent.add(textComponent);
    }

    @Override
    public String toString() {
        logger.info("Building string representation for TextComposite of type {}", type);
        StringBuilder stringBuilder = new StringBuilder();
        for (TextComponent child : textComponent) {
            if (child.getTypeComponent() == TypeTextComponent.PARAGRAPH) {
                logger.debug("Appending paragraph delimiter");
                stringBuilder.append(PARAGRAPH_SIGN);
            }
            stringBuilder.append(child.toString());
            if (child.getTypeComponent() == TypeTextComponent.LEXEME) {
                logger.debug("Appending space after lexeme");
                stringBuilder.append(SPACE_SIGN);
            }
        }
        return stringBuilder.toString();
    }

}
