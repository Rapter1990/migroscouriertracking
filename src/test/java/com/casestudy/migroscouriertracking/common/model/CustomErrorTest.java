package com.casestudy.migroscouriertracking.common.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the {@link CustomError.CustomSubError} class, ensuring proper creation
 * and retrieval of error subfield values in various scenarios.
 */
class CustomErrorTest {

    @Test
    public void testCustomSubErrorCreationWithAllFields() {

        // Given & When
        CustomError.CustomSubError subError = CustomError.CustomSubError.builder()
                .message("Field is required")
                .field("username")
                .value("user123")
                .type("ValidationError")
                .build();

        // Then
        assertEquals("Field is required", subError.getMessage(), "Message should match");
        assertEquals("username", subError.getField(), "Field should match");
        assertEquals("user123", subError.getValue(), "Value should match");
        assertEquals("ValidationError", subError.getType(), "Type should match");
    }

}
