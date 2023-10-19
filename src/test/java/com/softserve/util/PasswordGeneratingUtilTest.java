package com.softserve.util;

import com.softserve.service.UnitTestCategory;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.assertj.core.api.Assertions.assertThat;

@Category(UnitTestCategory.class)
public class PasswordGeneratingUtilTest {

    @Test
    public void testGeneratePassword() {
        assertThat(PasswordGeneratingUtil.generatePassword())
                .matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9])(?=.*[!@#$%^&*]).{8,}$");
    }

    @Test
    public void testIfEveryTimeGeneratedPasswordIsNew() {
        assertThat(PasswordGeneratingUtil.generatePassword()).isNotEqualTo(PasswordGeneratingUtil.generatePassword());
    }
}
