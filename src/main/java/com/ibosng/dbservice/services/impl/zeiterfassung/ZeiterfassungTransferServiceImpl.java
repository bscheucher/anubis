package com.ibosng.dbservice.services.impl.zeiterfassung;

import com.ibosng.dbservice.dtos.zeiterfassung.BasicSeminarDto;
import com.ibosng.dbservice.dtos.zeiterfassung.ZeiterfassungTransferDto;
import com.ibosng.dbservice.entities.seminar.Seminar;
import com.ibosng.dbservice.entities.zeiterfassung.ZeiterfassungStatus;
import com.ibosng.dbservice.entities.zeiterfassung.ZeiterfassungTransfer;
import com.ibosng.dbservice.exceptions.DBRuntimeException;
import com.ibosng.dbservice.repositories.zeiterfassung.ZeiterfassungTransferRepository;
import com.ibosng.dbservice.services.SeminarService;
import com.ibosng.dbservice.services.zeiterfassung.ZeiterfassungTransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ibosng.dbservice.utils.Parsers.parseDate;
import static com.ibosng.dbservice.utils.Parsers.parseDateTime;

@Service
@RequiredArgsConstructor
public class ZeiterfassungTransferServiceImpl implements ZeiterfassungTransferService {
    private final ZeiterfassungTransferRepository zeiterfassungTransferRepository;
    private final SeminarService seminarService;

    @Override
    public List<ZeiterfassungTransfer> findAll() {
        return zeiterfassungTransferRepository.findAll();
    }

    @Override
    public Optional<ZeiterfassungTransfer> findById(Integer id) {
        return zeiterfassungTransferRepository.findById(id);
    }

    @Override
    public ZeiterfassungTransfer save(ZeiterfassungTransfer object) {
        return zeiterfassungTransferRepository.save(object);
    }

    @Override
    public List<ZeiterfassungTransfer> saveAll(List<ZeiterfassungTransfer> objects) {
        return zeiterfassungTransferRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        zeiterfassungTransferRepository.deleteById(id);
    }

    @Override
    public List<ZeiterfassungTransfer> findAllByIdentifier(String identifier) {
        return List.of();
    }

    @Override
    public List<ZeiterfassungTransfer> findAllByStatus(ZeiterfassungStatus status) {
        return zeiterfassungTransferRepository.findAllByStatus(status);
    }

    @Override
    public List<ZeiterfassungTransferDto> findAllByStatusDtos(ZeiterfassungStatus status) {
        return zeiterfassungTransferRepository.findAllByStatus(status).stream().map(this::mapZeiterfassungTransferToDto).toList();
    }

    @Override
    public Page<ZeiterfassungTransferDto> findAllDtos(Pageable pageable) {
        return zeiterfassungTransferRepository.findAll(pageable).map(this::mapZeiterfassungTransferToDto);
    }
    @Override
    public ZeiterfassungTransferDto mapZeiterfassungTransferToDto(ZeiterfassungTransfer zeiterfassungTransfer) {
        ZeiterfassungTransferDto zeiterfassungTransferDto = new ZeiterfassungTransferDto();
        if(zeiterfassungTransfer.getDatumVon() != null) {
            zeiterfassungTransferDto.setDatumVon(zeiterfassungTransfer.getDatumVon().toString());
        }
        if(zeiterfassungTransfer.getDatumBis() != null) {
            zeiterfassungTransferDto.setDatumBis(zeiterfassungTransfer.getDatumBis().toString());
        }
        if(zeiterfassungTransfer.getDatumSent() != null) {
            zeiterfassungTransferDto.setDatumSent(zeiterfassungTransfer.getDatumSent().toString());
        }
        zeiterfassungTransferDto.setStatus(zeiterfassungTransfer.getStatus().name());
        zeiterfassungTransferDto.setUserName(zeiterfassungTransfer.getCreatedBy());
        zeiterfassungTransferDto.setSeminars(zeiterfassungTransfer.getSeminars().stream().map(seminar -> new BasicSeminarDto(seminar.getId(), seminar.getSeminarNummer(), seminar.getBezeichnung())).toList());
        return zeiterfassungTransferDto;
    }
    @Override
    public ZeiterfassungTransfer mapZeiterfassungTransferDtoToEntity(ZeiterfassungTransferDto zeiterfassungTransferDto) {
        ZeiterfassungTransfer zeiterfassungTransfer = new ZeiterfassungTransfer();
        if(zeiterfassungTransferDto.getDatumVon() != null && parseDate(zeiterfassungTransferDto.getDatumVon()) != null) {
            zeiterfassungTransfer.setDatumVon(parseDate(zeiterfassungTransferDto.getDatumVon()));
        }
        if(zeiterfassungTransferDto.getDatumBis() != null && parseDate(zeiterfassungTransferDto.getDatumBis()) != null) {
            zeiterfassungTransfer.setDatumBis(parseDate(zeiterfassungTransferDto.getDatumBis()));
        }
        if(zeiterfassungTransferDto.getDatumSent() != null && parseDate(zeiterfassungTransferDto.getDatumSent()) != null) {
            zeiterfassungTransfer.setDatumSent(parseDateTime(zeiterfassungTransferDto.getDatumSent()));
        }
        List<Seminar> seminars = new ArrayList<>();
        for(BasicSeminarDto seminarDto : zeiterfassungTransferDto.getSeminars()) {
            seminarService.findById(seminarDto.getId()).ifPresent(seminars::add);
        }
        zeiterfassungTransfer.setSeminars(seminars);
        return zeiterfassungTransfer;
    }

    @Override
    public void checkForOverlappingSeminar(ZeiterfassungTransferDto zeiterfassungTransferDto) {
        LocalDate datumVon = parseDate(zeiterfassungTransferDto.getDatumVon());
        LocalDate datumBis = parseDate(zeiterfassungTransferDto.getDatumBis());
        if (datumVon == null || datumBis == null) {
            throw new DBRuntimeException("DatumVon und DatumBis müssen angegeben werden.");
        }

        List<Integer> seminarIds = zeiterfassungTransferDto.getSeminars()
                .stream()
                .map(BasicSeminarDto::getId)
                .toList();

        List<ZeiterfassungStatus> statuses = List.of(ZeiterfassungStatus.COMPLETED, ZeiterfassungStatus.VALID, ZeiterfassungStatus.IN_PROGRESS);
        List<ZeiterfassungTransfer> overlappingEntries =
                zeiterfassungTransferRepository.findOverlappingEntriesForSeminars(seminarIds, datumVon, datumBis, statuses);

        if (!overlappingEntries.isEmpty()) {
            String message = overlappingEntries.stream()
                    .flatMap(entry -> entry.getSeminars().stream()
                            .filter(seminar -> seminarIds.contains(seminar.getId())) // Only consider relevant seminars
                            .map(seminar -> Map.entry(seminar.getBezeichnung(), entry))) // Pair seminar with entry
                    .collect(Collectors.groupingBy(Map.Entry::getKey)) // Group by seminar name
                    .entrySet()
                    .stream()
                    .map(entry -> {
                        String seminarName = entry.getKey();
                        String conflicts = entry.getValue().stream()
                                .map(e -> "[" + e.getValue().getDatumVon() + " - " + e.getValue().getDatumBis() + "]")
                                .collect(Collectors.joining(", "));
                        return "Overlaps found for Seminar: " + seminarName + ". Conflicting Dates: " + conflicts;
                    })
                    .collect(Collectors.joining("\n"));

            throw new DBRuntimeException(message);
        }
    }
}
