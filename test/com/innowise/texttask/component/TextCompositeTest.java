package com.innowise.texttask.component;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TextCompositeTest {

    @Test
    void addTextComponent() {
        TextComposite mainComposite = new TextComposite(TypeTextComponent.LEXEME);
        TextComposite addedComposite = new TextComposite(TypeTextComponent.WORD);
        mainComposite.addTextComponent(addedComposite);
        ArrayList<TextComponent> mainCompositeArray = mainComposite.getTextComponent();
        TypeTextComponent expected = TypeTextComponent.WORD;
        TypeTextComponent actual = mainCompositeArray.get(0).getTypeComponent();
        assertEquals(expected, actual);
    }

    @Test
    void testToString() {
    }
}