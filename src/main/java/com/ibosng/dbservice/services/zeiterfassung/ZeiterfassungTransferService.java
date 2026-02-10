package com.ibosng.dbservice.services.zeiterfassung;

import com.ibosng.dbservice.dtos.zeiterfassung.ZeiterfassungTransferDto;
import com.ibosng.dbservice.entities.zeiterfassung.ZeiterfassungStatus;
import com.ibosng.dbservice.entities.zeiterfassung.ZeiterfassungTransfer;
import com.ibosng.dbservice.services.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ZeiterfassungTransferService extends BaseService<ZeiterfassungTransfer> {
    List<ZeiterfassungTransfer> findAllByStatus(ZeiterfassungStatus status);
    List<ZeiterfassungTransferDto> findAllByStatusDtos(ZeiterfassungStatus status);
    Page<ZeiterfassungTransferDto> findAllDtos(Pageable pageable);
    ZeiterfassungTransferDto mapZeiterfassungTransferToDto(ZeiterfassungTransfer zeiterfassungTransfer);
    ZeiterfassungTransfer mapZeiterfassungTransferDtoToEntity(ZeiterfassungTransferDto zeiterfassungTransferDto);

    void checkForOverlappingSeminar(ZeiterfassungTransferDto zeiterfassungTransferDto);
}
