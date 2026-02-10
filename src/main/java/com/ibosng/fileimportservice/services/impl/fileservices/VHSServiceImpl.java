package com.ibosng.fileimportservice.services.impl.fileservices;

import com.ibosng.dbservice.entities.FileType;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerSource;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStatus;
import com.ibosng.dbservice.services.HeaderService;
import com.ibosng.dbservice.services.TeilnehmerStagingService;
import com.ibosng.fileimportservice.dtos.ParticipantVHSDto;
import com.ibosng.fileimportservice.dtos.SeminarDTO;
import com.ibosng.fileimportservice.exceptions.FieldTypeException;
import com.ibosng.fileimportservice.exceptions.ParserException;
import com.ibosng.fileimportservice.services.fileservices.VHSService;
import com.ibosng.microsoftgraphservice.dtos.FileDetails;
import com.ibosng.fileimportservice.utils.fileparsers.HeadersValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ibosng.fileimportservice.utils.Constants.FILE_IMPORT_SERVICE;
import static com.ibosng.fileimportservice.utils.Helpers.isNullOrBlank;
import static com.ibosng.fileimportservice.utils.fileparsers.VHSFileParserHelper.setCourseParticipantFields;

@Service
@Slf4j
@RequiredArgsConstructor
public class VHSServiceImpl implements VHSService {
    private final TeilnehmerStagingService teilnehmerStagingService;
    private final HeaderService headerService;

    @Override
    public void readFile(FileDetails file, Optional<String> filePassword) throws ParserException {
        try (Workbook workbook = WorkbookFactory.create(file.getFile(), filePassword.orElse(null))) {
            for (Sheet sheet : workbook) {
                List<SeminarDTO> seminarDTOList = mapProcessedSheet(sheet);
                List<TeilnehmerStaging> teilnehmerStagingList = new ArrayList<>();

                for (SeminarDTO seminarDTO : seminarDTOList) {
                    List<ParticipantVHSDto> participantVHSDtoList = seminarDTO.getSeminarParticipantDtoList();
                    for (ParticipantVHSDto participantVHSDto : participantVHSDtoList) {
                        TeilnehmerStaging teilnehmerStaging = getTeilnehmerStaging(file.getFilename(), participantVHSDto, seminarDTO);

                        teilnehmerStagingList.add(teilnehmerStaging);
                    }
                    if (teilnehmerStagingList.isEmpty()) {
                        log.warn("Malformed file : {}", file.getFilename());
                        throw new ParserException(String.format("Malformed file : %s", file.getFilename()));
                    }
                    teilnehmerStagingService.saveAll(teilnehmerStagingList);
                }
            }
            log.info("File {} was successfully read and data was saved.", file.getFilename());
        } catch (IOException | FieldTypeException e) {
            log.error("Error while reading VHS file: " + e.getMessage());
            throw new ParserException("Error while reading VHS file: ", e);
        }
    }


    @Override
    public TeilnehmerStaging getTeilnehmerStaging(String filePath, ParticipantVHSDto participantVHSDto, SeminarDTO seminarDTO) {
        TeilnehmerStaging teilnehmerStaging = new TeilnehmerStaging();
        teilnehmerStaging.setStatus(TeilnehmerStatus.NEW);
        teilnehmerStaging.setVorname(participantVHSDto.getVorname());
        teilnehmerStaging.setNachname(participantVHSDto.getNachname());
        teilnehmerStaging.setGeschlecht(participantVHSDto.getGeschlecht());
        teilnehmerStaging.setSvNummer(participantVHSDto.getSvNummer());
        teilnehmerStaging.setPlz(participantVHSDto.getPlz());
        teilnehmerStaging.setStrasse(participantVHSDto.getStrasse());
        teilnehmerStaging.setTelefon(participantVHSDto.getTelefon());
        teilnehmerStaging.setNation(participantVHSDto.getNation());
        teilnehmerStaging.setRgs(participantVHSDto.getRgs());
        teilnehmerStaging.setImportFilename(filePath);
        teilnehmerStaging.setInfo(participantVHSDto.getInfo());
        teilnehmerStaging.setOrt(participantVHSDto.getOrt());
        teilnehmerStaging.setAnmerkung(participantVHSDto.getAnmerkung());
        teilnehmerStaging.setTrainer(seminarDTO.getTrainer());
        teilnehmerStaging.setSeminarIdentifier(seminarDTO.getSeminarCode());
        teilnehmerStaging.setSeminarStartDate(seminarDTO.getSeminarDate());
        teilnehmerStaging.setSeminarStartTime(seminarDTO.getSeminarTime());
        teilnehmerStaging.setSeminarEndDate(seminarDTO.getKursende());
        teilnehmerStaging.setSeminarType(seminarDTO.getLevel());
        teilnehmerStaging.setSource(TeilnehmerSource.VHS);
        teilnehmerStaging.setCreatedBy(FILE_IMPORT_SERVICE);
        return teilnehmerStaging;
    }

    private List<SeminarDTO> mapProcessedSheet(Sheet sheet) throws FieldTypeException {
        List<ParticipantVHSDto> participantDTOS = new ArrayList<>();
        SeminarDTO seminarDTO = new SeminarDTO();
        List<SeminarDTO> courses = new ArrayList<>();
        try {
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum() + 2; rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                ParticipantVHSDto participantVHSDto = new ParticipantVHSDto();
                if (row != null) {
                    Cell firstCell = row.getCell(0);
                    Cell secondCell = row.getCell(1);
                    if ((firstCell != null && firstCell.getCellType() == CellType.STRING) || (secondCell != null && secondCell.getCellType() == CellType.STRING)) {
                        String firstCellValue = "";
                        if (firstCell != null) {
                            firstCellValue = firstCell.getStringCellValue();
                        }
                        if (firstCellValue.startsWith("Ibis")) {
                            log.debug("Header found: " + firstCellValue);

                            Pattern datePattern = Pattern.compile("Kursstart (\\d{2}\\.\\d{2}\\.\\d{4})");
                            Pattern timePattern = Pattern.compile("\\|(\\d{2}:\\d{2})\\s*Uhr\\s*\\|");
                            Pattern levelPattern = Pattern.compile("\\d{2}:\\d{2}\\s*Uhr\\s*\\|\\s*([^|]+)");

                            Matcher dateMatcher = datePattern.matcher(firstCellValue);
                            Matcher timeMatcher = timePattern.matcher(firstCellValue);
                            Matcher levelMatcher = levelPattern.matcher(firstCellValue);

                            String startDate = null;
                            String courseTime = null;
                            String courseLevel = null;

                            if (dateMatcher.find()) {
                                startDate = dateMatcher.group(1);
                            }
                            if (timeMatcher.find()) {
                                courseTime = timeMatcher.group(1).replace(" Uhr", "");
                            }
                            if (levelMatcher.find()) {
                                courseLevel = levelMatcher.group(1);
                            }
                            if (isNullOrBlank(startDate) || isNullOrBlank(courseTime) || isNullOrBlank(courseLevel)) {
                                log.warn("Cannot parse field in VHS parser");
                                throw new FieldTypeException("Cannot parse field in VHS parser");
                            }

                            // Print the extracted values
                            log.debug("Start Date: " + startDate);
                            log.debug("Course Time: " + courseTime);
                            log.debug("Course Level: " + courseLevel);

                            seminarDTO.setSeminarDate(startDate);

                            seminarDTO.setSeminarTime(courseTime);
                            seminarDTO.setLevel(courseLevel);

                            Cell participantCell = row.getCell(9);
                            seminarDTO.setSeminarCode(participantCell.getStringCellValue());
                        }
                        if (!firstCellValue.isEmpty() && firstCellValue.startsWith("Kursende")) {
                            log.debug("2nd row ");

                            Cell kursende = row.getCell(0);
                            seminarDTO.setKursende(kursende.getStringCellValue().trim());

                            String cellValue = kursende.getStringCellValue().trim();
                            Pattern pattern = Pattern.compile("\\d{2}\\.\\d{2}\\.\\d{4}");
                            Matcher matcher = pattern.matcher(cellValue);

                            if (matcher.find()) {
                                String extractedDate = matcher.group();
                                if (isNullOrBlank(extractedDate)) {
                                    log.warn("Cannot parse field in VHS parser: expected 'Kursende' in the cell.");
                                    throw new FieldTypeException("Cannot parse field in VHS parser: expected 'Kursende' in the cell.");
                                }
                                seminarDTO.setKursende(extractedDate);
                            }

                            Cell teacher = row.getCell(9);
                            seminarDTO.setTrainer(teacher.getStringCellValue());
                        }
                        String secondCellValue = "";
                        if (secondCell != null) {
                            secondCellValue = secondCell.getStringCellValue();
                        }
                        if (firstCellValue.startsWith("INFO") || secondCellValue.startsWith("Vorname")) {
                            HeadersValidator.xlsxHeadersValidator(row,
                                    headerService.getActiveHeadersNamesByFileType(FileType.VHS),
                                    headerService.getInactiveHeadersNamesByFileType(FileType.VHS));
                        }

                        if (firstCellValue.startsWith("AB") || firstCellValue.isEmpty() && !secondCellValue.isEmpty()) {
                            setCourseParticipantFields(participantVHSDto, row);

                            participantVHSDto.setSeminarId(seminarDTO.getSeminarCode());
                            participantDTOS.add(participantVHSDto);
                            seminarDTO.setSeminarParticipantDtoList(participantDTOS);
                        }
                    }
                } else if (rowIndex != sheet.getLastRowNum() + 2) {
                    seminarDTO.setSeminarParticipantDtoList(participantDTOS);
                    courses.add(seminarDTO);
                    participantDTOS = new ArrayList<>();
                    seminarDTO = new SeminarDTO();
                }
            }
        } catch (IllegalStateException | NullPointerException ex) {
            log.warn("Cannot parse field in VHS parser: {}", ex.getMessage());
            throw new FieldTypeException("Cannot parse field in VHS parser", ex);
        }
        return courses;
    }
}