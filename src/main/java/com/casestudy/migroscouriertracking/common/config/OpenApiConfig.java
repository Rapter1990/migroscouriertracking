package com.casestudy.migroscouriertracking.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Sercan Noyan Germiyanoğlu",
                        url = "https://github.com/Rapter1990/migroscouriertracking"
                ),
                description = "Case Study Migros - Courier Tracking",
                title = "migroscouriertracking",
                version = "1.0.0"
        )
)
public class OpenApiConfig {

}
