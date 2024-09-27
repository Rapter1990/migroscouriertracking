package com.casestudy.migroscouriertracking.common.model.dto.response;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomResponseTest {

    @Test
    public void testErrorOfCreatesCustomResponseWithErrorState() {

        // Given & When
        String errorResponse = "Error occurred";
        CustomResponse<String> response = CustomResponse.errorOf(errorResponse);

        // Then
        assertEquals(HttpStatus.OK, response.getHttpStatus(), "HTTP Status should be OK");
        assertEquals(false, response.getIsSuccess(), "isSuccess should be false for error responses");
        assertEquals(errorResponse, response.getResponse(), "Response should contain the error message");
        assertEquals(LocalDateTime.now().toLocalDate(), response.getTime().toLocalDate(), "Time should be close to current date");

    }

}
