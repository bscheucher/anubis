package com.ibosng.moxisservice.services;

import com.ibosng.dbservice.dtos.moxis.JobDescriptionDto;
import com.ibosng.dbservice.dtos.moxis.MoxisJobDto;
import com.ibosng.dbservice.dtos.moxis.MoxisUserDto;
import com.ibosng.dbservice.entities.moxis.MoxisJob;
import com.ibosng.moxisservice.exceptions.MoxisException;

import java.io.File;
import java.io.IOException;

public interface MoxisMapperService {
    boolean isHandySignaturExtern();
    JobDescriptionDto getJobDto(MoxisJobDto jobDto) throws MoxisException;

    MoxisJob mapAndSaveJob(JobDescriptionDto jobDescriptionDto, MoxisJobDto jobDto);

    File createJsonFileFromDto(Object dto, String filename) throws IOException;

    MoxisUserDto getMoxisUserDto(String user, boolean isHandySignatur);
}
