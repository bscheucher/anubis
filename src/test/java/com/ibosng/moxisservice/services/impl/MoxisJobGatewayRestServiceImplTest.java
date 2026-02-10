package com.ibosng.moxisservice.services.impl;

import com.ibosng.BaseIntegrationTest;
import com.ibosng.dbservice.dtos.moxis.MoxisJobDto;
import com.ibosng.dbservice.dtos.moxis.MoxisJobStateDto;
import com.ibosng.dbservice.dtos.moxis.MoxisUserDto;
import com.ibosng.dbservice.entities.moxis.MoxisJob;
import com.ibosng.dbservice.services.moxis.MoxisJobService;
import com.ibosng.moxisservice.clients.MoxisClientImpl;
import com.ibosng.moxisservice.exceptions.MoxisException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Disabled
class MoxisJobGatewayRestServiceImplTest extends BaseIntegrationTest {

    public static final String PROCESS_INSTANCE_ID = "123456";

    @Captor
    ArgumentCaptor<MultiValueMap> multiValueMapArgumentCaptor;

    @Captor
    ArgumentCaptor<MoxisJob> jobArgumentCaptor;

    @Mock
    private MoxisClientImpl moxisClientImpl;

    @Mock
    private MoxisJobService jobService;

    @InjectMocks
    private MoxisJobRestServiceImpl moxisJobService;

    @Test
    void startProcess() throws IOException {
        when(moxisClientImpl.startProcess(any(), true)).thenReturn(ResponseEntity.ok().build());

        MoxisJobDto jobDto = new MoxisJobDto();
        jobDto.setChangedBy("test@msg-plaut.hu");
        jobDto.setDescription("Test Job Description");
        moxisJobService.startProcess(jobDto, null, true);

        assertTrue(multiValueMapArgumentCaptor.getValue().containsKey("documentToSign"));
        assertTrue(multiValueMapArgumentCaptor.getValue().containsKey("jobDescription"));

        verify(jobService).save(jobArgumentCaptor.capture());
        MoxisJob capturedJob = jobArgumentCaptor.getValue();
        assertEquals("Test Job Description", capturedJob.getDescription());
        assertEquals("test@msg-plaut.hu", capturedJob.getChangedBy());
    }

    @Test
    void startProcess_clientException() throws IOException {
        when(moxisClientImpl.startProcess(any(), true)).thenThrow(new MoxisException("message", HttpStatus.BAD_REQUEST, "body", new Exception()));

        MoxisJobDto jobDto = new MoxisJobDto();
        jobDto.setChangedBy("test@msg-plaut.hu");
        jobDto.setDescription("Test Job Description");

        ResponseEntity<String> response = moxisJobService.startProcess(jobDto, null, true);

        assertTrue(multiValueMapArgumentCaptor.getValue().containsKey("documentToSign"));
        assertTrue(multiValueMapArgumentCaptor.getValue().containsKey("jobDescription"));

        assertTrue(response.hasBody());
        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    void getJobState() {
        MoxisJobStateDto jobStateDto = createMoxisJobStateDto();
        when(moxisClientImpl.getJobState(anyString(), any())).thenReturn(ResponseEntity.ok(jobStateDto));

        ResponseEntity<MoxisJobStateDto> response = moxisJobService.getJobState(PROCESS_INSTANCE_ID, "UPN");

        assertEquals(200, response.getStatusCode().value());
        assertEquals("FINISHED_SUCCESS", response.getBody().getState());
        assertEquals(PROCESS_INSTANCE_ID, response.getBody().getProcessInstanceId());
    }

    @Test
    void getJobState_clientException() {
        MoxisJobStateDto jobStateDto = createMoxisJobStateDto();
        when(moxisClientImpl.getJobState(anyString(), anyString())).thenThrow(new MoxisException("message", HttpStatus.BAD_REQUEST, "body", new Exception()));

        ResponseEntity<MoxisJobStateDto> response = moxisJobService.getJobState(PROCESS_INSTANCE_ID, "UPN");

        assertTrue(response.hasBody());
        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    void cancelJob() {
        String user = "user";
        when(moxisClientImpl.cancelJob(anyString(), any(MoxisUserDto.class))).thenReturn(ResponseEntity.ok().build());

        ResponseEntity<String> response = moxisJobService.cancelJob(PROCESS_INSTANCE_ID, user);

        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void cancelJob_clientException() {
        String user = "user";
        when(moxisClientImpl.cancelJob(anyString(), any(MoxisUserDto.class))).thenThrow(new MoxisException("message", HttpStatus.BAD_REQUEST, "body", new Exception()));

        ResponseEntity<String> response = moxisJobService.cancelJob(PROCESS_INSTANCE_ID, user);

        assertTrue(response.hasBody());
        assertEquals(400, response.getStatusCode().value());
    }

    private MoxisUserDto createUserDto() {
        MoxisUserDto userDto = new MoxisUserDto();
        userDto.setClassifier("UPN");
        userDto.setName("test@test.com");
        userDto.setRoleName("moxisSigner");
        userDto.setUserClass("com.xitrust.moxispe.api.User");
        return userDto;
    }

    private MoxisJobStateDto createMoxisJobStateDto() {
        MoxisJobStateDto jobStateDto = new MoxisJobStateDto();
        jobStateDto.setState("FINISHED_SUCCESS");
        jobStateDto.setProcessInstanceId(PROCESS_INSTANCE_ID);
        return jobStateDto;
    }

    private InputStream createInputStream(String filePath) {
        try {
            return new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}