package com.ibosng.teilnehmerportal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibosng.BaseIntegrationTest;
import com.ibosng.teilnehmerportal.exception.DocumentValidationException;
import com.ibosng.teilnehmerportal.exception.NatifApiException;
import com.ibosng.teilnehmerportal.service.TnDocumentUploadService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
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

    @Test
    void uploadFile_withValidFile_returnsSuccessEvents() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.pdf", MediaType.APPLICATION_PDF_VALUE, "pdf-content".getBytes()
        );

        String mockExternalResponse = "{\"status\":\"success\",\"data\":{\"start\":\"2026-02-10\",\"end\":\"2026-02-17\"}}";

        doAnswer(invocation -> {
            SseEmitter emitter = invocation.getArgument(1);

            emitter.send(SseEmitter.event()
                    .name("success")
                    .data(Map.of(
                            "message", "Upload completed",
                            "result", mockExternalResponse
                    )));
            return null;
        }).when(documentService).uploadDocument(any(MockMultipartFile.class), any(SseEmitter.class));

        mockMvc.perform(multipart("/tn-document/upload")
                        .file(file)
                        .param("type", "absence")
                        .param("identifier", "12345"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_EVENT_STREAM_VALUE))
                .andExpect(request().asyncStarted())
                .andReturn();

        verify(documentService).uploadDocument(any(MockMultipartFile.class), any(SseEmitter.class));
    }

    @Test
    void uploadFile_emptyFile_sendsErrorEventAndCompletes() throws Exception {
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file", "empty.pdf", MediaType.APPLICATION_PDF_VALUE, new byte[0]
        );

        mockMvc.perform(multipart("/tn-document/upload")
                        .file(emptyFile)
                        .param("type", "absence")
                        .param("identifier", "12345"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_EVENT_STREAM_VALUE))
                .andExpect(request().asyncStarted())
                .andReturn();

        verify(documentService, never()).uploadDocument(any(), any());
    }

    @Test
    void uploadFile_validationError_sendsErrorEvent() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.pdf", MediaType.APPLICATION_PDF_VALUE, "content".getBytes()
        );

        doThrow(new DocumentValidationException("File size exceeds 10MB limit"))
                .when(documentService).uploadDocument(any(MockMultipartFile.class), any(SseEmitter.class));

        mockMvc.perform(multipart("/tn-document/upload")
                        .file(file)
                        .param("type", "absence")
                        .param("identifier", "12345"))
                .andExpect(status().isOk())
                .andExpect(request().asyncStarted())
                .andReturn();

        verify(documentService).uploadDocument(any(MockMultipartFile.class), any(SseEmitter.class));
    }

    @Test
    void uploadFile_natifApiError_sendsErrorEvent() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.pdf", MediaType.APPLICATION_PDF_VALUE, "content".getBytes()
        );

        doThrow(new NatifApiException("Natif API returned error", 401, "Unauthorized access"))
                .when(documentService).uploadDocument(any(MockMultipartFile.class), any(SseEmitter.class));

        mockMvc.perform(multipart("/tn-document/upload")
                        .file(file)
                        .param("type", "absence")
                        .param("identifier", "12345"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_EVENT_STREAM_VALUE))
                .andExpect(request().asyncStarted())
                .andReturn();

        verify(documentService).uploadDocument(any(MockMultipartFile.class), any(SseEmitter.class));
    }

    @Test
    void uploadFile_unexpectedError_sendsErrorEvent() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.pdf", MediaType.APPLICATION_PDF_VALUE, "content".getBytes()
        );

        doThrow(new RuntimeException("Unexpected error"))
                .when(documentService).uploadDocument(any(MockMultipartFile.class), any(SseEmitter.class));

        mockMvc.perform(multipart("/tn-document/upload")
                        .file(file)
                        .param("type", "absence")
                        .param("identifier", "12345"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_EVENT_STREAM_VALUE))
                .andExpect(request().asyncStarted())
                .andReturn();

        verify(documentService).uploadDocument(any(MockMultipartFile.class), any(SseEmitter.class));
    }

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