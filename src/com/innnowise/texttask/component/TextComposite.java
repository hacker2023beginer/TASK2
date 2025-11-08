package com.innnowise.texttask.component;

import java.util.ArrayList;

public class TextComposite extends TextComponent{
    private final ArrayList<TextComponent> textComponent = new ArrayList<>();
    private static final String PARAGRAPH_SIGN = "\n";
    private static final String SPACE_SIGN = " ";

    public TextComposite(TypeTextComponent type) {
        this.type = type;
    }

    public void addTextComponent(TextComponent textComponent){
        this.textComponent.add(textComponent);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (TextComponent textComponent : textComponent) {
            if (textComponent.getTypeComponent() == TypeTextComponent.PARAGRAPH) {
                stringBuilder.append(PARAGRAPH_SIGN);
            }
            stringBuilder.append(textComponent.toString());
            if (textComponent.getTypeComponent() == TypeTextComponent.LEXEME) {
                stringBuilder.append(SPACE_SIGN);
            }
        }
        return stringBuilder.toString();
    }
}
