package com.ibosng.gatewayservice.utils;

import com.ibosng.BaseIntegrationTest;
import com.ibosng.gatewayservice.enums.FileUploadTypes;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HelpersTest extends BaseIntegrationTest {

    @Test
    void testCheckResultIfNull() {
        ResponseEntity<Object> responseEntity = Helpers.checkResultIfNull(null);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    void testAreAllFieldsNull() {
        assertTrue(Helpers.areAllFieldsNull(null));

        TestDto dto = new TestDto(null, null);
        assertTrue(Helpers.areAllFieldsNull(dto));

        TestDto dtoNonNull = new TestDto("value1", "value2");
        assertFalse(Helpers.areAllFieldsNull(dtoNonNull));
    }


    @Test
    void testGetTokenFromRequest() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer token123");
        assertEquals("token123", Helpers.getTokenFromRequest(request));

        when(request.getHeader("Authorization")).thenReturn(null);
        assertNull(Helpers.getTokenFromRequest(request));

        when(request.getHeader("Authorization")).thenReturn("invalidHeader");
        assertNull(Helpers.getTokenFromRequest(request));
    }

    @Test
    void testGetTokenFromAuthorizationHeader() {
        assertEquals("token123", Helpers.getTokenFromAuthorizationHeader("Bearer token123"));

        assertNull(Helpers.getTokenFromAuthorizationHeader("invalidHeader"));
    }

    @Test
    void testAreAllFieldsNullCatchBlock() {
        Field mockField = mock(Field.class);

        TestDto dto = new TestDto(null, null);

        try {
            when(mockField.get(any())).thenThrow(new IllegalAccessException("Test exception"));

            Field[] fields = TestDto.class.getDeclaredFields();
            fields[0] = mockField;

            assertTrue(Helpers.areAllFieldsNull(dto));
        } catch (IllegalAccessException e) {
            fail("Unexpected IllegalAccessException", e);
        }
    }

    @Test
    void testExtractErrorMessage_validJsonWithDetail() {
        String responseBody = "{\"detail\": \"Error message\"}";

        String errorMessage = Helpers.extractErrorMessage(responseBody);

        assertEquals("Error message", errorMessage);
    }

    @Test
    void testCreateErrorResponse() {
        HttpStatus status = HttpStatus.NOT_FOUND;
        String errorMessage = "Resource not found";

        ResponseEntity<String> responseEntity = Helpers.createErrorResponse(status, errorMessage);

        assertEquals(status, responseEntity.getStatusCode());
        assertEquals(errorMessage, responseEntity.getBody());
    }

    @Test
    void testGetFileName() {
        String identifier = "ID123";
        String additionalIdentifier = "ADD456";

        String formattedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        String resultWithAdditional = Helpers.getFileName(identifier, additionalIdentifier, FileUploadTypes.VORDIENSTZEITEN_NACHWEIS, true);
        assertEquals(formattedDate + "_VORDIENSTZEITEN_NACHWEIS_ADD456_ID123", resultWithAdditional);

        String resultWithoutAdditional = Helpers.getFileName(identifier, null, FileUploadTypes.ECARD, true);
        assertEquals(formattedDate + "_ECARD_ID123", resultWithoutAdditional);
    }


    @Test
    void testGetFileExtension_knownMimeTypes() throws IOException {

        InputStream jpegStream = new ByteArrayInputStream(new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF}); // JPEG signature
        InputStream defaultStream = new ByteArrayInputStream(new byte[]{'D', 'E', 'F'}); // Default signature

        assertEquals(".jpeg", Helpers.getFileExtension(jpegStream, "test.jpg"));

        assertEquals(".txt", Helpers.getFileExtension(defaultStream, "defaultStream.def"));
    }

    private static class TestDto {
        private String field1;
        private String field2;

        TestDto(String field1, String field2) {
            this.field1 = field1;
            this.field2 = field2;
        }
    }
}
