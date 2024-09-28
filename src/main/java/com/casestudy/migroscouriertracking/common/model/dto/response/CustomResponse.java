package com.casestudy.migroscouriertracking.common.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * A generic response wrapper class named {@link CustomResponse<T>} for API responses.
 * This class provides a standardized format for API responses,
 * including metadata such as the response time, HTTP status,
 * success status, and the actual response data.
 *
 * @param <T> the type of the response data
 */
@Getter
@Builder
public class CustomResponse<T> {

    @Builder.Default
    private LocalDateTime time = LocalDateTime.now();

    private HttpStatus httpStatus;

    private Boolean isSuccess;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T response;

    /**
     * A constant representing a successful response with no data.
     */
    public static final CustomResponse<Void> SUCCESS = CustomResponse.<Void>builder()
            .httpStatus(HttpStatus.OK)
            .isSuccess(true)
            .build();

    /**
     * Creates a successful response with the provided data.
     *
     * @param response the response data
     * @param <T>     the type of the response data
     * @return a CustomResponse containing the success status and response data
     */
    public static <T> CustomResponse<T> successOf(final T response) {
        return CustomResponse.<T>builder()
                .httpStatus(HttpStatus.OK)
                .isSuccess(true)
                .response(response)
                .build();
    }

    /**
     * Creates an error response with the provided data.
     *
     * @param response the response data indicating the error
     * @param <T>     the type of the response data
     * @return a CustomResponse containing the error status and response data
     */
    public static <T> CustomResponse<T> errorOf(final T response) {
        return CustomResponse.<T>builder()
                .httpStatus(HttpStatus.OK)
                .isSuccess(false)
                .response(response)
                .build();
    }

}
