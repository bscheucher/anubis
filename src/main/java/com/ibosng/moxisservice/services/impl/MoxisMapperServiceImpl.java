package com.ibosng.moxisservice.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ibosng.dbibosservice.services.BenutzerIbosService;
import com.ibosng.dbservice.dtos.moxis.*;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.dbservice.entities.mitarbeiter.vereinbarung.Vereinbarung;
import com.ibosng.dbservice.entities.moxis.*;
import com.ibosng.dbservice.entities.workflows.WWorkflow;
import com.ibosng.dbservice.services.BenutzerService;
import com.ibosng.dbservice.services.VereinbarungService;
import com.ibosng.dbservice.services.mitarbeiter.StammdatenService;
import com.ibosng.dbservice.services.mitarbeiter.VertragsdatenService;
import com.ibosng.dbservice.services.moxis.MoxisJobService;
import com.ibosng.dbservice.services.workflows.WWorkflowService;
import com.ibosng.moxisservice.exceptions.MoxisException;
import com.ibosng.moxisservice.services.MoxisEnvironmentService;
import com.ibosng.moxisservice.services.MoxisMapperService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;
import static com.ibosng.moxisservice.utils.Constants.MOXIS_SERVICE;
import static com.ibosng.moxisservice.utils.Helpers.capitalize;

@Slf4j
@Service
@RequiredArgsConstructor
public class MoxisMapperServiceImpl implements MoxisMapperService {
    private final static String ON_PLACEHOLDER = "ON_PLACEHOLDER";

    private final ObjectMapper objectMapper;
    private final MoxisJobService jobService;
    private final BenutzerService benutzerService;
    private final BenutzerIbosService benutzerIbosService;
    private final StammdatenService stammdatenService;
    private final VertragsdatenService vertragsdatenService;
    private final WWorkflowService wWorkflowService;
    private final MoxisEnvironmentService moxisEnvironmentService;
    private final VereinbarungService vereinbarungService;


    @Getter
    @Value("${testMoxisInternalSignee:#{null}}")
    private String testMoxisInternalSignee;

    @Getter
    @Value("${moxisConstituent:#{null}}")
    private String moxisConstituent;

    @Getter
    @Value("${moxisVereinbarungSigningPeriod:#{null}}")
    private String moxisVereinbarungSigningPeriod;

    @Setter
    private boolean isHandySignaturExtern = false;

    @Override
    public boolean isHandySignaturExtern() {
        return isHandySignaturExtern;
    }

    @Override
    public JobDescriptionDto getJobDto(MoxisJobDto jobDto) throws MoxisException {
        log.info("Mapping for personalnummer {}, isProduction {}", jobDto.getPersonalNummer(), moxisEnvironmentService.isProduction());
        if (isNullOrBlank(jobDto.getPersonalNummer())) {
            throw new MoxisException("Personalnummer must not be null, job will not be send.", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        JobDescriptionDto jobDescriptionDto = new JobDescriptionDto();
        jobDescriptionDto.setPositionType(ON_PLACEHOLDER);
        JobMetaDataDto metaDataDto = new JobMetaDataDto();
        if (jobDto.getSigningJobType().equals(SigningJobType.VEREINBARUNG)) {
            Vereinbarung vereinbarung = vereinbarungService.findVereinbarungByWorkflow_Id(jobDto.getWorkflow());
            if (vereinbarung != null) {
                metaDataDto.setReferenceId(String.valueOf(vereinbarung.getId()));
                metaDataDto.setExpirationDate(OffsetDateTime.now().plusDays(Integer.valueOf(moxisVereinbarungSigningPeriod)).truncatedTo(ChronoUnit.SECONDS));
            } else {
                throw new MoxisException(String.format("No Vereinbarung found for Workflow: %S", jobDto.getWorkflow()), HttpStatus.UNPROCESSABLE_ENTITY);
            }
        } else {
            metaDataDto.setReferenceId(jobDto.getPersonalNummer());
        }


        if (!isNullOrBlank(jobDto.getDescription())) {
            metaDataDto.setDescription(jobDto.getDescription());
        } else {
            metaDataDto.setDescription(String.format("New contract for personalnummer: %s", jobDto.getPersonalNummer()));
        }


        jobDescriptionDto.setMetaData(metaDataDto);

        Stammdaten stammdaten = stammdatenService.findByPersonalnummerString(jobDto.getPersonalNummer());
        if (stammdaten == null) {
            throw new MoxisException(String.format("Stammdaten are not found for personal nummer: %s", jobDto.getPersonalNummer()), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (!isNullOrBlank(getMoxisConstituent())) {
            jobDescriptionDto.setConstituent(getMoxisUserDto(getMoxisConstituent(), true));
        } else {
            throw new MoxisException(String.format("Constituent must not be null, job will not be send for personalnummer: %s", jobDto.getPersonalNummer()), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        List<IterationDto> iterationDtoList = new ArrayList<>();
        IterationDto firstIteration;
        if (!isNullOrBlank(stammdaten.getVorname()) && !isNullOrBlank(stammdaten.getNachname()) && !isNullOrBlank(stammdaten.getEmail()) && stammdaten.getMobilnummer() != null && stammdaten.getMobilnummer().getTelefonnummer() != null) {
            firstIteration = setFirstIterationSignees(stammdaten, jobDto.getSigningJobType());
        } else {
            throw new MoxisException(String.format("New employee data must not be null, job will not be send for personalnummer: %s", jobDto.getPersonalNummer()), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        IterationDto internalInvitees = new IterationDto();
        internalInvitees.setCategory(Category.QSIG.getType());
        internalInvitees.setIterationNumber(1);
        List<String> internalSignees;
        Vertragsdaten vertragsdaten = vertragsdatenService.findByPersonalnummerString(stammdaten.getPersonalnummer().getPersonalnummer()).stream().findFirst().orElse(null);
        if (vertragsdaten != null && vertragsdaten.getKostenstelle() != null && vertragsdaten.getKostenstelle().getNummer() != null) {
            if (moxisEnvironmentService.isProduction()) {
                internalSignees = benutzerIbosService.getSigneesFromKostenStelle(vertragsdaten.getKostenstelle().getNummer());
            } else {
                internalSignees = new ArrayList<>();
                internalSignees.add(getTestMoxisInternalSignee());
            }
        } else {
            throw new MoxisException(String.format("Kostenstelle must not be null, job will not be send for personalnummer: %s", jobDto.getPersonalNummer()), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        for (String internalSignee : internalSignees) {
            MoxisUserForRoleDto internalUser = new MoxisUserForRoleDto();
            internalUser.setName(internalSignee);
            internalUser.setRoleName("moxisSigner");
            internalInvitees.setInvitees(new ArrayList<>());
            internalInvitees.getInvitees().add(internalUser);
        }

        iterationDtoList.add(firstIteration);
        iterationDtoList.add(internalInvitees);
        jobDescriptionDto.setIterationData(iterationDtoList);

        if (jobDto.getAdditionalRecipients() != null && !jobDto.getAdditionalRecipients().isEmpty()) {
            jobDescriptionDto.setAdditionalRecipients(getAdditionalRecipients(jobDto));
        }
        return jobDescriptionDto;
    }

    private IterationDto setFirstIterationSignees(Stammdaten stammdaten, SigningJobType jobType) {
        if (SigningJobType.CONTRACT.equals(jobType)) {
            return getExternalInvitees(stammdaten);
        }
        if(SigningJobType.ZUSATZ.equals(jobType) || SigningJobType.VEREINBARUNG.equals(jobType)) {
            IterationDto firstIteration = new IterationDto();
            MoxisUserForRoleDto employee = new MoxisUserForRoleDto();
            employee.setName(stammdaten.getVorname() + "." + stammdaten.getNachname());
            employee.setRoleName("moxisSigner");
            firstIteration.setCategory(Category.QSIG.getType());
            firstIteration.setInvitees(new ArrayList<>());
            firstIteration.getInvitees().add(employee);
            firstIteration.setIterationNumber(0);
            return firstIteration;
        }
        return null;
    }

    private List<AddressBookEntryDto> getAdditionalRecipients(MoxisJobDto jobDto) {
        List<AddressBookEntryDto> additionalRecipients = new ArrayList<>();

        for (String recipientEmail : jobDto.getAdditionalRecipients()) {
            Benutzer recipientUser = benutzerService.findByEmail(recipientEmail);
            if (recipientUser != null) {
                AddressBookEntryDto recipient = new AddressBookEntryDto();
                recipient.setName(recipientUser.getFirstName() + " " + recipientUser.getLastName());
                recipient.setEmail(recipientUser.getEmail());
                additionalRecipients.add(recipient);
            } else {
                log.warn("Recipient not found in benutzer for email addresse: {} for the personal nummer: {}", recipientEmail, jobDto.getPersonalNummer());
            }
        }
        return additionalRecipients;
    }

    @Override
    public MoxisUserDto getMoxisUserDto(String user, boolean isHandySignatur) {
        MoxisUserDto moxisUserDto = new MoxisUserDto();
        if (isHandySignatur) {
            moxisUserDto.setClassifier(UserClassifier.UPN.getType());
            if (!user.contains("@")) {
                moxisUserDto.setName(user);
            } else {
                moxisUserDto.setName(user.substring(0, user.indexOf('@')));
            }
        } else {
            moxisUserDto.setClassifier(UserClassifier.EMAIL.getType());
            if (!user.contains("@")) {
                String[] parts = user.split("\\.");
                Benutzer benutzer = benutzerService.findAllByFirstNameAndLastName(capitalize(parts[0]), capitalize(parts[1]));
                if (benutzer != null) {
                    moxisUserDto.setName(benutzer.getEmail());
                } else {
                    throw new MoxisException(String.format("Consituent not found in Benutzer for user: %s", user), HttpStatus.UNPROCESSABLE_ENTITY);
                }
            } else {
                moxisUserDto.setName(user);
            }
        }
        moxisUserDto.setUserClass(UserClass.USER.getType());
        return moxisUserDto;
    }

    private IterationDto getExternalInvitees(Stammdaten stammdaten) {
        IterationDto externalInvitees = new IterationDto();
        externalInvitees.setIterationNumber(0);
        ExternalMoxisUserForRoleDto externalUser = new ExternalMoxisUserForRoleDto();
        if (stammdaten.isHandySignatur()) {
            externalUser.setUserClass(UserClass.EXTERNAL_USER_FOR_ROLE.getType());
            externalInvitees.setCategory(Category.EXTERNAL_QSIG.getType());
            setHandySignaturExtern(true);
        } else {
            externalUser.setUserClass(UserClass.EXTERNAL_APPROVAL_FOR_ROLE.getType());
            externalInvitees.setCategory(Category.EXTERNAL_APPROVAL.getType());
            setHandySignaturExtern(false);
        }
        externalUser.setRoleName(RoleName.EXTERN.getType());
        externalUser.setName(stammdaten.getEmail());

        externalUser.setExternalUserName(stammdaten.getVorname() + " " + stammdaten.getNachname());
        externalUser.setPhoneNumber(stammdaten.getMobilnummer().getLand().getTelefonvorwahl() + stammdaten.getMobilnummer().getTelefonnummer());
        externalInvitees.setInvitees(Collections.singletonList(externalUser));
        return externalInvitees;
    }

    @Override
    public MoxisJob mapAndSaveJob(JobDescriptionDto jobDescriptionDto, MoxisJobDto jobDto) {
        checkForActiveSigningJobs(jobDto);
        Optional<WWorkflow> workflow = wWorkflowService.findById(jobDto.getWorkflow());
        if (workflow.isEmpty()) {
            log.warn("No workflow found with id: {}", jobDto.getWorkflow());
            throw new MoxisException(String.format("No workflow found with id: %s", jobDto.getWorkflow()), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        MoxisJob job = new MoxisJob();
        job.setWorkflow(workflow.get());
        job.setDescription(jobDescriptionDto.getMetaData().getDescription());
        job.setReferenceId(jobDescriptionDto.getMetaData().getReferenceId());
        job.setConstituent(getMoxisConstituent());
        job.setPositionType(PositionType.valueOf(jobDescriptionDto.getPositionType()));
        job.setExpirationDate(jobDescriptionDto.getMetaData().getExpirationDate());
        job.setIterationDataList(mapIterationDataList(jobDescriptionDto.getIterationData(), job));
        job.setCreatedBy(MOXIS_SERVICE);
        job.setStatus(MoxisJobStatus.NEW);
        job.setSigningJobType(jobDto.getSigningJobType());
        job = jobService.save(job);
        return job;
    }

    private void checkForActiveSigningJobs(MoxisJobDto jobDto) {
        List<MoxisJob> previousJobs = jobService.findAllByReferenceIdAndWorkflow_IdAndStatus(jobDto.getPersonalNummer(), jobDto.getWorkflow(), List.of(MoxisJobStatus.IN_PROGRESS, MoxisJobStatus.PENDING));
        if (!previousJobs.isEmpty()) {
            log.warn("Active signing jobs already exists for personalnummer {} and workflow {}", jobDto.getPersonalNummer(), jobDto.getWorkflow());
            throw new MoxisException(String.format("Signing jobs already exists for personalnummer: %s and workflow %s", jobDto.getPersonalNummer(), jobDto.getWorkflow()), HttpStatus.CONFLICT);
        }
    }

    private List<IterationData> mapIterationDataList(List<IterationDto> iterationDtos, MoxisJob job) {
        return iterationDtos.stream()
                .map(iterationDto -> mapIterationData(iterationDto, job))
                .collect(Collectors.toList());
    }

    private IterationData mapIterationData(IterationDto iterationDto, MoxisJob job) {
        IterationData iterationData = new IterationData();
        iterationData.setCategory(iterationDto.getCategory());
        iterationData.setIterationNumber(iterationDto.getIterationNumber());
        iterationData.setMoxisJob(job);
        iterationData.setInvitees(mapInviteeList(iterationDto.getInvitees(), iterationData));
        iterationData.setCreatedBy(MOXIS_SERVICE);
        return iterationData;
    }

    private List<Invitee> mapInviteeList(List<MoxisUserDto> invitees, IterationData iterationData) {
        return invitees.stream()
                .map((MoxisUserDto moxisUser) -> mapInvitee(moxisUser, iterationData))
                .collect(Collectors.toList());
    }

    private Invitee mapInvitee(MoxisUserDto moxisUser, IterationData iterationData) {
        Invitee invitee = new Invitee();
        invitee.setClassifier(UserClassifier.fromType(moxisUser.getClassifier()));
        invitee.setRoleName(moxisUser.getRoleName());
        invitee.setUserClass(UserClass.fromType(moxisUser.getUserClass()));
        invitee.setName(moxisUser.getName());
        invitee.setIterationData(iterationData);
        invitee.setCreatedBy(MOXIS_SERVICE);
        return invitee;
    }


    public File createJsonFileFromDto(Object dto, String filename) throws IOException {
        // Replace the extension with .json
        String baseName = filename.contains(".") ? filename.substring(0, filename.lastIndexOf('.')) : filename;
        String jsonFilename = baseName + ".json";
        // Use system's temporary directory and append the given filename
        File tempFile = new File(System.getProperty("java.io.tmpdir"), jsonFilename);
        try (FileWriter fileWriter = new FileWriter(tempFile)) {
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            String jsonString = objectMapper.writeValueAsString(dto);
            fileWriter.write(jsonString);
        } catch (IOException e) {
            log.error("Error while creating JSON file from dto: {}", e.getMessage());
            throw e;
        }
        return tempFile;
    }


    public static boolean deleteLocalFile(String filePath) {
        File file = new File(filePath);
        return file.delete();
    }
}
