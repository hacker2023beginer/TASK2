package com.innowise.texttask.service;

import com.innowise.texttask.component.TextComposite;

import java.util.List;

public interface SentenceSortByLexemeCountService {
    List<TextComposite> sortSentencesByLexemeCount(TextComposite textComposite);
}

