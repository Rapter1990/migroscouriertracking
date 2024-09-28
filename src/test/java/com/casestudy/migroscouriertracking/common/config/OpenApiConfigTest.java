package com.casestudy.migroscouriertracking.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for verifying OpenAPI configuration in {@link OpenApiConfig}.
 * Tests the correctness of {@link OpenAPIDefinition} and its metadata.
 */
class OpenApiConfigTest {

    @Test
    void openApiInfo() {

        // Given
        OpenAPIDefinition openAPIDefinition = OpenApiConfig.class.getAnnotation(OpenAPIDefinition.class);

        // Then
        assertEquals("1.0.0", openAPIDefinition.info().version());
        assertEquals("migroscouriertracking", openAPIDefinition.info().title());
        assertEquals(
                "Case Study Migros - Courier Tracking",
                openAPIDefinition.info().description()
        );
    }


    @Test
    void contactInfo() {

        // Given
        Info info = OpenApiConfig.class.getAnnotation(OpenAPIDefinition.class).info();
        Contact contact = info.contact();

        // Then
        assertEquals("Sercan Noyan Germiyanoğlu", contact.name());
        assertEquals("https://github.com/Rapter1990/migroscouriertracking", contact.url());
    }

}
