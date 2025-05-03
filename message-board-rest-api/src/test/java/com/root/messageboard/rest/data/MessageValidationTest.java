package com.root.messageboard.rest.data;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class MessageValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenAllFieldsValid_thenNoViolations() {
        Message msg = new Message(
                "Hello",
                "This is a valid content.",
                "Alice",
                "https://example.com");
        Set<ConstraintViolation<Message>> violations = validator.validate(msg);
        assertThat(violations).isEmpty();
    }

    @Test
    void whenTitleIsBlank_thenViolation() {
        Message msg = new Message(
                "",
                "Content",
                "Bob",
                "https://example.com");
        Set<ConstraintViolation<Message>> violations = validator.validate(msg);
        assertThat(violations)
                .extracting("propertyPath")
                .anyMatch(path -> path.toString().equals("title"));
    }

    @Test
    void whenUrlIsInvalid_thenViolation() {
        Message msg = new Message(
                "Title",
                "Content",
                "Bob",
                "not-a-url");
        Set<ConstraintViolation<Message>> violations = validator.validate(msg);
        assertThat(violations)
                .extracting("propertyPath")
                .anyMatch(path -> path.toString().equals("url"));
    }
}
