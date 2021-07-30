package com.softserve.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class TranslatorTest {
    @Spy
    private HashMap<String, HashMap<Locale, String>> dictionary;

    @Spy
    @InjectMocks
    private Translator translator;

    @Test
    public void getTranslateTest() {
        String word = "word";
        Locale language = Locale.UK;
        String expectedWord = "�����";
        HashMap<Locale, String> map = new HashMap(){{
            put(language, expectedWord);
        }};
        dictionary.put(word, map);

        String result = translator.getTranslation(word, language);

        assertEquals(expectedWord, result);
    }

    @Test
    public void getTranslateIfLanguageNotExistsTest() {
        String expectedWord = "word";
        Locale language = Locale.UK;
        String word = "�����";
        HashMap<Locale, String> map = new HashMap(){{ put(language, word); }};
        dictionary.put(expectedWord, map);

        Locale nonExistLanguage = Locale.JAPAN;

        String result = translator.getTranslation(expectedWord, nonExistLanguage);

        assertEquals(expectedWord, result);
    }
}
