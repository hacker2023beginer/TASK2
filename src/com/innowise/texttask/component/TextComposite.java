package com.innowise.texttask.component;

import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TextComposite extends AbstractTextComponent {
    private static final Logger logger = LogManager.getLogger();
    private static final String PARAGRAPH_SIGN = "\n";
    private static final String SPACE_SIGN = " ";
    private final ArrayList<AbstractTextComponent> abstractTextComponent = new ArrayList<>();

    public TextComposite(TypeTextComponent type) {
        logger.debug("Creating TextComposite with type {}", type);
        this.type = type;
    }

    public ArrayList<AbstractTextComponent> getChildren() {
        ArrayList<AbstractTextComponent> copyArrayOfAbstractTextComponent = new ArrayList<>(abstractTextComponent);
        return copyArrayOfAbstractTextComponent;
    }

    public void add(AbstractTextComponent abstractTextComponent){
        logger.debug("Add TextComponent with type {}", abstractTextComponent.getTypeComponent());
        this.abstractTextComponent.add(abstractTextComponent);
    }

    @Override
    public String toString() {
        logger.info("Building string representation for TextComposite of type {}", type);
        StringBuilder stringBuilder = new StringBuilder();
        for (AbstractTextComponent child : abstractTextComponent) {
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
