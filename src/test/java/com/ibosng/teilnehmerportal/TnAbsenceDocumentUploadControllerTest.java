package com.ibosng.teilnehmerportal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibosng.BaseIntegrationTest;
import com.ibosng.teilnehmerportal.service.TnDocumentUploadService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
public class TnAbsenceDocumentUploadControllerTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TnDocumentUploadService documentService;

    // --- /tn-document/upload tests ---

    @Test
    void uploadFile_withFile_returnsExtractedDataFromExternalApi() throws Exception {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.pdf", MediaType.APPLICATION_PDF_VALUE, "pdf-content".getBytes()
        );

        String mockExternalResponse = "{\"status\":\"success\",\"data\":{\"start\":\"2026-02-10\",\"end\":\"2026-02-17\"}}";

        when(documentService.uploadDocument(any()))
                .thenReturn(mockExternalResponse);

        // Act & Assert
        mockMvc.perform(multipart("/tn-document/upload")
                        .file(file)
                        .param("type", "absence")
                        .param("identifier", "12345"))
                .andExpect(status().isOk())
                .andExpect(content().json(mockExternalResponse));
    }

    @Test
    void uploadFile_emptyFile_returnsBadRequest() throws Exception {
        // Arrange
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file", "empty.pdf", MediaType.APPLICATION_PDF_VALUE, new byte[0]
        );

        // Act & Assert
        mockMvc.perform(multipart("/tn-document/upload")
                        .file(emptyFile)
                        .param("type", "absence")
                        .param("identifier", "12345"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("File must not be empty"));
    }

    @Test
    void uploadFile_externalApiError_returnsForwardedError() throws Exception {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.pdf", MediaType.APPLICATION_PDF_VALUE, "content".getBytes()
        );

        // Create a WebClientResponseException with 401 status
        WebClientResponseException unauthorizedException = WebClientResponseException.create(
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized",
                HttpHeaders.EMPTY,
                "Unauthorized access".getBytes(StandardCharsets.UTF_8),
                StandardCharsets.UTF_8
        );

        when(documentService.uploadDocument(any()))
                .thenThrow(unauthorizedException);

        // Act & Assert
        mockMvc.perform(multipart("/tn-document/upload")
                        .file(file)
                        .param("type", "absence")
                        .param("identifier", "12345"))
                .andExpect(status().isUnauthorized());
    }

    // --- /tn-document/confirm tests ---

    @Test
    void confirmData_returnsSubmittedData() throws Exception {
        Map<String, String> data = Map.of(
                "start", "2026-01-01",
                "end", "2026-01-08",
                "type", "absence"
        );

        mockMvc.perform(post("/tn-document/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(data)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.start").value("2026-01-01"))
                .andExpect(jsonPath("$.type").value("absence"));
    }
}