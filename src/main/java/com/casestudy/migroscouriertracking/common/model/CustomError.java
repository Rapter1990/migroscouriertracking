package com.casestudy.migroscouriertracking.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * A class named {@link CustomError} that represents a custom error response.
 */
@Getter
@Builder
public class CustomError {

    @Builder.Default
    private LocalDateTime time = LocalDateTime.now();

    private HttpStatus httpStatus;

    private String header;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    @Builder.Default
    private final Boolean isSuccess = false;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<CustomSubError> subErrors;

    /**
     * A class named {@link CustomSubError} representing a sub-error within a {@link CustomError}.
     */
    @Getter
    @Builder
    public static class CustomSubError {

        private String message;

        private String field;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Object value;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String type;

    }

    /**
     * An enumeration class named {@link Header} representing different headers for custom errors.
     */
    @Getter
    @RequiredArgsConstructor
    public enum Header {

        API_ERROR("API ERROR"),

        NOT_FOUND("NOT EXIST"),

        BAD_REQUEST("BAD REQUEST"),

        VALIDATION_ERROR("VALIDATION ERROR"),

        PROCESS_ERROR("PROCESS ERROR");


        private final String name;

    }

}
