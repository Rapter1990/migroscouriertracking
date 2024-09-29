package com.casestudy.migroscouriertracking.common.exception;

import com.casestudy.migroscouriertracking.base.AbstractRestControllerTest;
import com.casestudy.migroscouriertracking.common.model.CustomError;
import com.casestudy.migroscouriertracking.courier.exception.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for verifying the behavior of {@link GlobalExceptionHandler}.
 * Tests various exception handling scenarios to ensure correct HTTP responses and error messages.
 */
class GlobalExceptionHandlerTest extends AbstractRestControllerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    @DisplayName("Given MethodArgumentNotValidException - When HandleMethodArgumentNotValid - Then Return RespondWithBadRequest")
    void givenMethodArgumentNotValidException_whenHandleMethodArgumentNotValid_thenReturnRespondWithBadRequest() {

        // Given
        BindingResult bindingResult = mock(BindingResult.class);
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);
        FieldError fieldError = new FieldError("objectName", "fieldName", "error message");
        List<ObjectError> objectErrors = Collections.singletonList(fieldError);

        when(bindingResult.getAllErrors()).thenReturn(objectErrors);

        CustomError expectedError = CustomError.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .header(CustomError.Header.VALIDATION_ERROR.getName())
                .message("Validation failed")
                .subErrors(Collections.singletonList(
                        CustomError.CustomSubError.builder()
                                .field("fieldName")
                                .message("error message")
                                .build()))
                .build();

        // When
        ResponseEntity<Object> responseEntity = globalExceptionHandler.handleMethodArgumentNotValid(ex);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        CustomError actualError = (CustomError) responseEntity.getBody();
        checkCustomError(expectedError, actualError);

    }

    @Test
    @DisplayName("Given MethodArgumentNotValidException - When HandleMethodArgumentNotValidWithObjectError - Return RespondWithBadRequest")
    void givenMethodArgumentNotValidException_whenHandleMethodArgumentNotValid_thenRespondWithBadRequest() {

        // Given
        BindingResult bindingResult = mock(BindingResult.class);
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);
        FieldError fieldError = new FieldError("objectName", "fieldName", "error message");
        List<ObjectError> objectErrors = Collections.singletonList(fieldError);

        when(bindingResult.getAllErrors()).thenReturn(objectErrors);

        CustomError expectedError = CustomError.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .header(CustomError.Header.VALIDATION_ERROR.getName())
                .message("Validation failed")
                .subErrors(Collections.singletonList(
                        CustomError.CustomSubError.builder()
                                .field("fieldName")
                                .message("error message")
                                .build()))
                .build();

        // When
        ResponseEntity<Object> responseEntity = globalExceptionHandler.handleMethodArgumentNotValid(ex);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        CustomError actualError = (CustomError) responseEntity.getBody();
        checkCustomError(expectedError, actualError);

    }

    @Test
    @DisplayName("Given ConstraintViolationException - When HandlePathVariableErrors - Return RespondWithBadRequest")
    void givenConstraintViolationException_whenHandlePathVariableErrors_thenReturnRespondWithBadRequest() {

        // Given
        ConstraintViolation<String> mockViolation = mock(ConstraintViolation.class);
        Path mockPath = mock(Path.class);
        Set<ConstraintViolation<?>> violations = Set.of(mockViolation);
        ConstraintViolationException mockException = new ConstraintViolationException(violations);

        CustomError.CustomSubError subError = CustomError.CustomSubError.builder()
                .message("must not be null")
                .field("")
                .value("invalid value")
                .type("String") // Default to String if getRootBeanClass() is null
                .build();

        CustomError expectedError = CustomError.builder()
                .time(LocalDateTime.now())
                .httpStatus(HttpStatus.BAD_REQUEST)
                .header(CustomError.Header.VALIDATION_ERROR.getName())
                .message("Constraint violation")
                .subErrors(Collections.singletonList(subError))
                .build();

        // When
        when(mockViolation.getMessage()).thenReturn("must not be null");
        when(mockViolation.getPropertyPath()).thenReturn(mockPath);
        when(mockPath.toString()).thenReturn("field");
        when(mockViolation.getInvalidValue()).thenReturn("invalid value");
        when(mockViolation.getRootBeanClass()).thenReturn(String.class); // Ensure this does not return null

        // Then
        ResponseEntity<Object> responseEntity = globalExceptionHandler.handlePathVariableErrors(mockException);

        CustomError actualError = (CustomError) responseEntity.getBody();

        // Verify
        checkCustomError(expectedError, actualError);

    }

    @Test
    @DisplayName("Given RuntimeException - When HandleRuntimeException - Return RespondWithNotFound")
    void givenRuntimeException_whenHandleRuntimeException_thenReturnRespondWithNotFound() {

        // Given
        RuntimeException ex = new RuntimeException("Runtime exception message");

        CustomError expectedError = CustomError.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .header(CustomError.Header.API_ERROR.getName())
                .message("Runtime exception message")
                .build();

        // When
        ResponseEntity<?> responseEntity = globalExceptionHandler.handleRuntimeException(ex);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        CustomError actualError = (CustomError) responseEntity.getBody();
        checkCustomError(expectedError, actualError);

    }

    @Test
    @DisplayName("Given CourierNotFoundException - When HandleCourierNotFound - Then Return RespondWithNotFound")
    void givenCourierNotFoundException_whenHandleCourierNotFound_thenReturnRespondWithNotFound() {

        // Given
        CourierNotFoundException ex = new CourierNotFoundException("Courier not found message");

        CustomError expectedError = CustomError.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .header(CustomError.Header.API_ERROR.getName())
                .message("Courier not found message")
                .isSuccess(false)
                .build();

        // When
        ResponseEntity<CustomError> responseEntity = globalExceptionHandler.handleCourierNotFound(ex);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        CustomError actualError = responseEntity.getBody();
        checkCustomError(expectedError, actualError);
    }

    @Test
    @DisplayName("Given StoreFarAwayException - When HandleStoreFarAway - Then Return RespondWithBadRequest")
    void givenStoreFarAwayException_whenHandleStoreFarAway_thenReturnRespondWithBadRequest() {

        // Given
        StoreFarAwayException ex = new StoreFarAwayException("Store is too far away");

        CustomError expectedError = CustomError.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .header(CustomError.Header.API_ERROR.getName())
                .message("Store is too far away")
                .isSuccess(false)
                .build();

        // When
        ResponseEntity<CustomError> responseEntity = globalExceptionHandler.handleStoreFarAway(ex);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        CustomError actualError = responseEntity.getBody();
        checkCustomError(expectedError, actualError);
    }

    @Test
    @DisplayName("Given StoreNotFoundException - When HandleStoreNotFound - Then Return RespondWithNotFound")
    void givenStoreNotFoundException_whenHandleStoreNotFound_thenReturnRespondWithNotFound() {

        // Given
        StoreNotFoundException ex = new StoreNotFoundException("Store not found message");

        CustomError expectedError = CustomError.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .header(CustomError.Header.API_ERROR.getName())
                .message("Store not found message")
                .isSuccess(false)
                .build();

        // When
        ResponseEntity<CustomError> responseEntity = globalExceptionHandler.handleStoreNotFound(ex);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        CustomError actualError = responseEntity.getBody();
        checkCustomError(expectedError, actualError);
    }

    @Test
    @DisplayName("Given TimestampBeforeStoreCreateException - When HandleTimestampBeforeStoreCreate - Then Return RespondWithBadRequest")
    void givenTimestampBeforeStoreCreateException_whenHandleTimestampBeforeStoreCreate_thenReturnRespondWithBadRequest() {

        // Given
        TimestampBeforeStoreCreateException ex = new TimestampBeforeStoreCreateException("Timestamp is before store creation");

        CustomError expectedError = CustomError.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .header(CustomError.Header.API_ERROR.getName())
                .message("Timestamp is before store creation")
                .isSuccess(false)
                .build();

        // When
        ResponseEntity<CustomError> responseEntity = globalExceptionHandler.handleTimestampBeforeStoreCreate(ex);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        CustomError actualError = responseEntity.getBody();
        checkCustomError(expectedError, actualError);
    }

    @Test
    @DisplayName("Given StoreReentryTooSoonException - When HandleStoreReentryTooSoon - Then Return RespondWithConflict")
    void givenStoreReentryTooSoonException_whenHandleStoreReentryTooSoon_thenReturnRespondWithConflict() {

        // Given
        StoreReentryTooSoonException ex = new StoreReentryTooSoonException("Reentry to the store is too soon");

        CustomError expectedError = CustomError.builder()
                .httpStatus(HttpStatus.CONFLICT)
                .header(CustomError.Header.API_ERROR.getName())
                .message("Reentry to the store is too soon")
                .isSuccess(false)
                .build();

        // When
        ResponseEntity<CustomError> responseEntity = globalExceptionHandler.handleStoreReentryTooSoon(ex);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        CustomError actualError = responseEntity.getBody();
        checkCustomError(expectedError, actualError);
    }


    private void checkCustomError(CustomError expectedError, CustomError actualError) {

        assertThat(actualError).isNotNull();
        assertThat(actualError.getTime()).isNotNull();
        assertThat(actualError.getHeader()).isEqualTo(expectedError.getHeader());
        assertThat(actualError.getIsSuccess()).isEqualTo(expectedError.getIsSuccess());

        if (expectedError.getMessage() != null) {
            assertThat(actualError.getMessage()).isEqualTo(expectedError.getMessage());
        }

        if (expectedError.getSubErrors() != null) {
            assertThat(actualError.getSubErrors().size()).isEqualTo(expectedError.getSubErrors().size());
            if (!expectedError.getSubErrors().isEmpty()) {
                assertThat(actualError.getSubErrors().get(0).getMessage()).isEqualTo(expectedError.getSubErrors().get(0).getMessage());
                assertThat(actualError.getSubErrors().get(0).getField()).isEqualTo(expectedError.getSubErrors().get(0).getField());
                assertThat(actualError.getSubErrors().get(0).getValue()).isEqualTo(expectedError.getSubErrors().get(0).getValue());
                assertThat(actualError.getSubErrors().get(0).getType()).isEqualTo(expectedError.getSubErrors().get(0).getType());
            }
        }
    }

}