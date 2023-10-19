package com.softserve.util;

import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;

public final class PasswordGeneratingUtil {

    private static final CharacterData SPECIAL_CHARS = new CharacterData() {
        public String getErrorCode() {
            return "INSUFFICIENT_SPECIAL";
        }

        public String getCharacters() {
            return "!@#$%^&*";
        }
    };
    private static final int PASSWORD_LENGTH = 30;
    private static final CharacterRule SPECIAL_CHARS_RULE = new CharacterRule(SPECIAL_CHARS);
    private static final CharacterRule LOWER_CASE_RULE = new CharacterRule(EnglishCharacterData.LowerCase);
    private static final CharacterRule DIGIT_RULE = new CharacterRule(EnglishCharacterData.Digit);
    private static final CharacterRule UPPER_CASE_RULE = new CharacterRule(EnglishCharacterData.UpperCase);

    private PasswordGeneratingUtil() {
    }

    public static String generatePassword() {
        return new PasswordGenerator().generatePassword(
                PASSWORD_LENGTH,
                SPECIAL_CHARS_RULE,
                LOWER_CASE_RULE,
                UPPER_CASE_RULE,
                DIGIT_RULE
        );
    }
}
