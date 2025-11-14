package com.innowise.texttask.main;

import com.innowise.texttask.component.TextComposite;
import com.innowise.texttask.component.TypeTextComponent;
import com.innowise.texttask.exception.CustomTextException;
import com.innowise.texttask.parser.*;
import com.innowise.texttask.reader.TextFileReader;
import com.innowise.texttask.reader.impl.TextFileReaderImpl;
import com.innowise.texttask.service.MaxSentencesWithEqualWordsService;
import com.innowise.texttask.service.SentenceSortByLexemeCountService;
import com.innowise.texttask.service.SwapFirstLastLexemeService;
import com.innowise.texttask.service.impl.TextMaxSentencesWithEqualWordsService;
import com.innowise.texttask.service.impl.TextSentenceSortByLexemeCountService;
import com.innowise.texttask.service.impl.TextSwapFirstLastLexemeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    private static final Logger logger = LogManager.getLogger();
    public static void main(String[] args) {
        logger.info("Start main");

        try {
            Path filePath = Paths.get("data/text.txt");
            TextFileReader reader = new TextFileReaderImpl();
            List<String> lines = reader.readFromFile(filePath);

            StringBuilder textBuilder = new StringBuilder();
            for (String line : lines) {
                textBuilder.append(line).append("\n");
            }
            String text = textBuilder.toString().trim();

            logger.info("Original text: {}", text);

            SymbolParser symbolParser = new SymbolParser();
            WordParser wordParser = new WordParser(symbolParser);
            LexemeParser lexemeParser = new LexemeParser(wordParser);
            SentenceParser sentenceParser = new SentenceParser(lexemeParser);
            ParagraphParser paragraphParser = new ParagraphParser(sentenceParser);

            TextComposite textComposite = new TextComposite(TypeTextComponent.PARAGRAPH);
            paragraphParser.parseComposite(text, textComposite);
            logger.info("Text parsed successfully");

            MaxSentencesWithEqualWordsService maxSentencesService = new TextMaxSentencesWithEqualWordsService();
            int maxSentences = maxSentencesService.findMaxSentencesWithEqualWords(textComposite);
            logger.info("Finding maximum number of sentences with equal words: {}", maxSentences);

            logger.info("Sorting sentences by lexeme count (ascending)");
            SentenceSortByLexemeCountService sortService = new TextSentenceSortByLexemeCountService();
            List<TextComposite> sortedSentences = sortService.sortSentencesByLexemeCount(textComposite);
            logger.info("   Total sentences: {}", sortedSentences.size());

            for (int i = 0; i < sortedSentences.size(); i++) {
                TextComposite sentence = sortedSentences.get(i);
                int lexemeCount = countLexemes(sentence);
                logger.info("   Sentence {} ({} lexemes): {}",
                        i + 1, lexemeCount, sentence.toString().trim());
            }

            logger.info("Swapping first and last lexemes in each sentence");
            SwapFirstLastLexemeService swapService = new TextSwapFirstLastLexemeService();
            TextComposite swappedText = swapService.swapFirstLastLexemeInSentences(textComposite);
            logger.info("Modified text: {}", swappedText);
            logger.info("All operations completed successfully!");

        } catch (CustomTextException e) {
            logger.fatal("Error: {}", e.getMessage());
        }
    }

    private static int countLexemes(TextComposite sentence) {
        int count = 0;
        for (com.innowise.texttask.component.AbstractTextComponent component : sentence.getChildren()) {
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
}

