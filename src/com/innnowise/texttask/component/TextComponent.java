package com.innnowise.texttask.component;

public abstract class TextComponent {
    protected TypeTextComponent type;

    public TypeTextComponent getTypeComponent() {
        return this.type;
    }
    public void setTypeComponent(TypeTextComponent type) {
        this.type = type;
    }

    public abstract String toString();
}
