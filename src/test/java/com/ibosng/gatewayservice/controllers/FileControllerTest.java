package com.ibosng.gatewayservice.controllers;

import com.ibosng.gatewayservice.dtos.response.PayloadResponse;
import com.ibosng.gatewayservice.enums.FileUploadTypes;
import com.ibosng.gatewayservice.services.BenutzerDetailsService;
import com.ibosng.gatewayservice.services.DeleteFileService;
import com.ibosng.gatewayservice.services.DownloadService;
import com.ibosng.gatewayservice.services.UploadService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@Disabled
@ExtendWith(MockitoExtension.class)
public class FileControllerTest {

    @Mock
    private BenutzerDetailsService benutzerDetailsService;

    @Mock
    private UploadService uploadService;

    @Mock
    private DownloadService downloadService;

    @Mock
    private DeleteFileService deleteFileService;

    @InjectMocks
    private FileController fileController;


    @Test
    public void testUploadFileSuccess() {
        MultipartFile file = new MockMultipartFile("file", "report.jrxml", MediaType.TEXT_XML_VALUE, "template content".getBytes());

        when(benutzerDetailsService.isUserEligible(anyString(), anyList())).thenReturn(true);
        when(uploadService.uploadFile(any(MultipartFile.class),anyString(), anyString(), anyString())).thenReturn(createSuccessfulPayload());

        ResponseEntity<PayloadResponse> response = fileController.uploadFile("Bearer authorizationHeader", file, "type", "identifier", "null");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(benutzerDetailsService, times(1)).isUserEligible(anyString(), anyList());
        verify(uploadService, times(1)).uploadFile(any(MultipartFile.class), anyString(), anyString(), anyString());
    }

    @Test
    public void testUploadFileForbidden() {
        MultipartFile file = new MockMultipartFile("file", "report.jrxml", MediaType.TEXT_XML_VALUE, "template content".getBytes());

        when(benutzerDetailsService.isUserEligible(anyString(), anyList())).thenReturn(false);

        ResponseEntity<PayloadResponse> response = fileController.uploadFile("Bearer authorizationHeader", file, "type", "identifier", "null");

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(benutzerDetailsService, times(1)).isUserEligible(anyString(), anyList());
    }

    @Test
    public void testDownloadFileForbidden() throws IOException {
        when(benutzerDetailsService.isUserEligible(anyString(), anyList())).thenReturn(false);

        ResponseEntity<byte[]> response = fileController.downloadFile("Bearer dummy", "123", "ecard", "null");

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(benutzerDetailsService, times(1)).isUserEligible(anyString(), anyList());
    }

    @Test
    public void testDownloadFileSuccess() {
        when(benutzerDetailsService.isUserEligible(anyString(), anyList())).thenReturn(true);
        when(downloadService.downloadFileWithResponse(anyString(), anyString(), any(FileUploadTypes.class))).thenReturn(ResponseEntity.ok().build());
        ResponseEntity<byte[]> response = fileController.downloadFile(
                "Bearer authorizationHeader", "ecard", "identifier", "additionalIdentifier");

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteFileSuccess() {
        when(benutzerDetailsService.isUserEligible(anyString(), anyList())).thenReturn(true);
        doNothing().when(deleteFileService).deleteFile(anyString(), anyString(), any(FileUploadTypes.class));

        ResponseEntity<byte[]> response = fileController.deleteFile(
                "Bearer authorizationHeader", "ecard", "identifier", "additionalIdentifier");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(benutzerDetailsService, times(1)).isUserEligible(anyString(), anyList());
        verify(deleteFileService, times(1)).deleteFile(eq("identifier"), eq("additionalIdentifier"), eq(FileUploadTypes.fromValue("ecard")));
    }

    @Test
    public void testDeleteFileForbidden() throws IOException {
        when(benutzerDetailsService.isUserEligible(anyString(), anyList())).thenReturn(false);

        ResponseEntity<byte[]> response = fileController.deleteFile("Bearer dummy", "123", "ecard", "null");

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(benutzerDetailsService, times(1)).isUserEligible(anyString(), anyList());
    }

    private PayloadResponse createSuccessfulPayload () {
        PayloadResponse payloadResponse = new PayloadResponse();
        payloadResponse.setSuccess(true);

        return payloadResponse;
    }

}
