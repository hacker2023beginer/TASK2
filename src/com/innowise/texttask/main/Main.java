package com.innowise.texttask.main;

import com.innowise.texttask.component.TextComposite;
import com.innowise.texttask.component.TypeTextComponent;
import com.innowise.texttask.exception.CustomTextException;
import com.innowise.texttask.parser.*;
import com.innowise.texttask.reader.TextFileReader;
import com.innowise.texttask.reader.impl.TextFileReaderImpl;
import com.innowise.texttask.service.MaxSentencesWithEqualWordsService;
import com.innowise.texttask.service.SentenceSortByLexemeCountService;
import com.innowise.texttask.service.SentenceWithEqualWordService;
import com.innowise.texttask.service.SwapFirstLastLexemeService;
import com.innowise.texttask.service.impl.TextMaxSentencesWithEqualWordsService;
import com.innowise.texttask.service.impl.TextSentenceSortByLexemeCountService;
import com.innowise.texttask.service.impl.TextSentenceWithEqualWordService;
import com.innowise.texttask.service.impl.TextSwapFirstLastLexemeService;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Text Processing Application ===\n");

        try {
            Path filePath = Paths.get("data/text.txt");
            TextFileReader reader = new TextFileReaderImpl();
            List<String> lines = reader.readFromFile(filePath);

            StringBuilder textBuilder = new StringBuilder();
            for (String line : lines) {
                textBuilder.append(line).append("\n");
            }
            String text = textBuilder.toString().trim();
            
            System.out.println("Original text:");
            System.out.println(text);
            System.out.println("\n" + repeatString("=", 80) + "\n");

            SymbolParser symbolParser = new SymbolParser();
            WordParser wordParser = new WordParser(symbolParser);
            LexemeParser lexemeParser = new LexemeParser(wordParser);
            SentenceParser sentenceParser = new SentenceParser(lexemeParser);
            ParagraphParser paragraphParser = new ParagraphParser(sentenceParser);

            TextComposite textComposite = new TextComposite(TypeTextComponent.PARAGRAPH);
            paragraphParser.parseComposite(text, textComposite);

            System.out.println("Text parsed successfully!\n");
            System.out.println(repeatString("=", 80) + "\n");

            System.out.println("1. Finding maximum number of sentences with equal words:");
            MaxSentencesWithEqualWordsService maxSentencesService = new TextMaxSentencesWithEqualWordsService();
            int maxSentences = maxSentencesService.findMaxSentencesWithEqualWords(textComposite);
            System.out.println("   Result: " + maxSentences + " sentences contain the same word");
            System.out.println();

            System.out.println("2. Finding number of sentences with equal words:");
            SentenceWithEqualWordService equalWordService = new TextSentenceWithEqualWordService();
            int sentencesWithEqualWords = equalWordService.findNumOfSentencesWithEqualWord(textComposite);
            System.out.println("   Result: " + sentencesWithEqualWords + " sentences have at least one common word");
            System.out.println();

            System.out.println("3. Sorting sentences by lexeme count (ascending):");
            SentenceSortByLexemeCountService sortService = new TextSentenceSortByLexemeCountService();
            List<TextComposite> sortedSentences = sortService.sortSentencesByLexemeCount(textComposite);
            System.out.println("   Total sentences: " + sortedSentences.size());
            for (int i = 0; i < sortedSentences.size(); i++) {
                TextComposite sentence = sortedSentences.get(i);
                int lexemeCount = countLexemes(sentence);
                System.out.println("   Sentence " + (i + 1) + " (" + lexemeCount + " lexemes): " + 
                                 sentence.toString().trim());
            }
            System.out.println();

            System.out.println("4. Swapping first and last lexemes in each sentence:");
            SwapFirstLastLexemeService swapService = new TextSwapFirstLastLexemeService();
            TextComposite swappedText = swapService.swapFirstLastLexemeInSentences(textComposite);
            System.out.println("   Modified text:");
            System.out.println(swappedText.toString());
            System.out.println();

            System.out.println(repeatString("=", 80));
            System.out.println("All operations completed successfully!");

        } catch (CustomTextException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static int countLexemes(TextComposite sentence) {
        int count = 0;
        for (com.innowise.texttask.component.AbstractTextComponent component : sentence.getTextComponent()) {
            if (component instanceof TextComposite) {
                TextComposite composite = (TextComposite) component;
                if (composite.getTypeComponent() == TypeTextComponent.LEXEME) {
                    count++;
                } else {
                    count += countLexemes(composite);
                }
            }
        }
        return count;
    }

    private static String repeatString(String str, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
}

