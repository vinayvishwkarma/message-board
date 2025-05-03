package com.root.messageboard.core.dto;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class MessageRequestDTOTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

    private final Validator validator = factory.getValidator();

    @Test
    void testValidMessageRequestDTO() {
        MessageRequestDTO validMessage = new MessageRequestDTO(
                "Title",
                "Content",
                "Sender",
                "https://example.com");
        Set<ConstraintViolation<MessageRequestDTO>> violations = validator.validate(validMessage);
        assertTrue(violations.isEmpty(), "There should be no validation violations");
    }

    @Test
    void testInvalidMessageRequestDTO_TitleBlank() {
        MessageRequestDTO invalidMessage = new MessageRequestDTO(
                "",
                "Content",
                "Sender",
                "https://example.com");
        Set<ConstraintViolation<MessageRequestDTO>> violations = validator.validate(invalidMessage);
        assertFalse(violations.isEmpty(), "Title cannot be blank");
    }

    @Test
    void testInvalidMessageRequestDTO_InvalidURL() {
        MessageRequestDTO invalidMessage = new MessageRequestDTO(
                "Title",
                "Content",
                "Sender",
                "invalid-url");
        Set<ConstraintViolation<MessageRequestDTO>> violations = validator.validate(invalidMessage);
        assertFalse(violations.isEmpty(), "URL should be valid");
    }
}
