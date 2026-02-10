package com.ibosng.teilnehmerportal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibosng.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Map;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class TnAbsenceDocumentUploadControllerTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // --- /tn-document/upload tests ---

    @Test
    @WithMockUser(username = "testuser")
    void uploadFile_withFile_returnsStartAndEndDates() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.pdf", MediaType.APPLICATION_PDF_VALUE, "pdf-content".getBytes()
        );

        LocalDate today = LocalDate.now();
        LocalDate oneWeekLater = today.plusWeeks(1);

        mockMvc.perform(multipart("/tn-document/upload")
                        .file(file)
                        .param("type", "absence")
                        .param("identifier", "12345")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.start").value(today.toString()))
                .andExpect(jsonPath("$.end").value(oneWeekLater.toString()));
    }

    @Test
    @WithMockUser(username = "testuser")
    void uploadFile_withoutFile_returnsStartAndEndDates() throws Exception {
        LocalDate today = LocalDate.now();
        LocalDate oneWeekLater = today.plusWeeks(1);

        mockMvc.perform(multipart("/tn-document/upload")
                        .param("type", "absence")
                        .param("identifier", "12345")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.start").value(today.toString()))
                .andExpect(jsonPath("$.end").value(oneWeekLater.toString()));
    }

    @Test
    @WithMockUser(username = "testuser")
    void uploadFile_missingTypeParam_returnsBadRequest() throws Exception {
        mockMvc.perform(multipart("/tn-document/upload")
                        .param("identifier", "12345")
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "testuser")
    void uploadFile_missingIdentifierParam_returnsBadRequest() throws Exception {
        mockMvc.perform(multipart("/tn-document/upload")
                        .param("type", "absence")
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    // --- /tn-document/confirm tests ---

    @Test
    @WithMockUser(username = "testuser")
    void confirmData_returnsSubmittedData() throws Exception {
        Map<String, String> data = Map.of(
                "start", "2026-01-01",
                "end", "2026-01-08",
                "type", "absence"
        );

        mockMvc.perform(post("/tn-document/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(data))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.start").value("2026-01-01"))
                .andExpect(jsonPath("$.end").value("2026-01-08"))
                .andExpect(jsonPath("$.type").value("absence"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void confirmData_emptyBody_returnsBadRequest() throws Exception {
        mockMvc.perform(post("/tn-document/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("")
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }
}