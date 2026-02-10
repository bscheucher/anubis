package com.ibosng.dbibosservice.services.impl;

import com.ibosng.dbibosservice.dtos.BasicPersonDto;
import com.ibosng.dbibosservice.dtos.BasicProjectDto;
import com.ibosng.dbibosservice.dtos.revenue.ProjectForecastDataDto;
import com.ibosng.dbibosservice.dtos.revenue.ProjectRevenueDataDto;
import com.ibosng.dbibosservice.entities.BenutzerIbos;
import com.ibosng.dbibosservice.repositories.BenutzerIbosRepository;
import com.ibosng.dbibosservice.services.BenutzerIbosService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ibosng.dbibosservice.utils.Parsers.mapLocalDateRow;

@Service
public class BenutzerIbosServiceImpl implements BenutzerIbosService {

    private final BenutzerIbosRepository benutzerIbosRepository;

    public BenutzerIbosServiceImpl(BenutzerIbosRepository benutzerIbosRepository) {
        this.benutzerIbosRepository = benutzerIbosRepository;
    }

    @Override
    public List<BenutzerIbos> findAll() {
        return benutzerIbosRepository.findAll();
    }

    @Override
    public Optional<BenutzerIbos> findById(Integer id) {
        return benutzerIbosRepository.findById(id);
    }

    @Override
    public BenutzerIbos save(BenutzerIbos object) {
        return benutzerIbosRepository.save(object);
    }

    @Override
    public List<BenutzerIbos> saveAll(List<BenutzerIbos> objects) {
        return benutzerIbosRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        benutzerIbosRepository.deleteById(id);

    }

    @Override
    public List<String> findRolesForUser(String user) {
        return benutzerIbosRepository.findRolesForUser(user);
    }

    @Override
    public List<BasicProjectDto> findActiveProjectForUser(String user) {
        List<Object[]> rawData = benutzerIbosRepository.findActiveProjectsForUser(user);
        return rawData.stream()
                .map(this::mapBasicProjectDto)
                .toList();
    }

    @Override
    public List<BasicProjectDto> findPastProjectForUser(String user) {
        List<Object[]> rawData = benutzerIbosRepository.findPastProjectsForUser(user);
        return rawData.stream()
                .map(this::mapBasicProjectDto)
                .toList();
    }

    @Override
    public List<BasicProjectDto> findFutureProjectForUser(String user) {
        List<Object[]> rawData = benutzerIbosRepository.findFutureProjectsForUser(user);
        return rawData.stream()
                .map(this::mapBasicProjectDto)
                .toList();
    }

    @Override
    public ProjectRevenueDataDto findRevenueAndHoursFromProjectAndDates(Integer projectNumber, LocalDate von, LocalDate bis, boolean isProjectStartToToday, boolean isTodayToProjectEnd) {
        if (isProjectStartToToday && !isTodayToProjectEnd) {
            return findRevenueAndHoursFromProjectStartToToday(projectNumber);
        }
        if (!isProjectStartToToday && isTodayToProjectEnd) {
            return findRevenueAndHoursFromTodayToProjectEnd(projectNumber);
        }
        Object[] isSollData = findIsAndSollRevenueBetweenDates(projectNumber, von, bis);
        ProjectRevenueDataDto projectRevenueDataDto = mapDataToProjectRevenueDataDto(isSollData);
        return calculateRevenueAndHoursForProject(projectNumber, projectRevenueDataDto, von, bis);
    }

    @Override
    public ProjectForecastDataDto findProjectForecast(Integer projectNumber) {
        Object[] forecastData = findForecastRevenue(projectNumber);
        ProjectForecastDataDto projectForecastDataDto = mapDataToForecastDataDto(forecastData);
        ProjectRevenueDataDto projectRevenueDataDto = findRevenueAndHoursFromProjectStartToToday(projectNumber);
        setForecastDataAndDeviations(projectForecastDataDto, projectRevenueDataDto);
        return projectForecastDataDto;
    }

    @Override
    public List<BasicPersonDto> getAllFuehrungskraftForMitarbeiter() {
        List<Object[]> rawObjects = benutzerIbosRepository.getAllFuehrungskraftForMitarbeiter();
        List<BasicPersonDto> results = new ArrayList<>();
        rawObjects.forEach(data -> results.add(mapDataToBasicPersonDto(data)));
        return results;
    }

    @Override
    public String getEmailForFuehrungskraftForMitarbeiter(Long adresseId) {
        return benutzerIbosRepository.getEmailForFuehrungskraftForMitarbeiter(adresseId);
    }

    @Override
    public String getEmailForStartcoachForMitarbeiter(Long adresseId) {
        return benutzerIbosRepository.getEmailForStartcoachForMitarbeiter(adresseId);
    }

    @Override
    public List<BasicPersonDto> getAllStartcoachForMitarbeiter() {
        List<Object[]> rawObjects = benutzerIbosRepository.getAllStartcoachForMitarbeiter();
        List<BasicPersonDto> results = new ArrayList<>();
        rawObjects.forEach(data -> results.add(mapDataToBasicPersonDto(data)));
        return results;
    }

    @Override
    public Integer getTitelId(String titel) {
        return benutzerIbosRepository.getTitelId(titel);
    }

    @Override
    public Integer saveTitel(String titel) {
        return benutzerIbosRepository.saveTitel(titel);
    }

    @Override
    public String getTitelFromId(Integer id) {
        return benutzerIbosRepository.getTitelFromId(id);
    }

    @Override
    public List<String> getSigneesFromKostenStelle(Integer kostenstelle) {
        return benutzerIbosRepository.getSigneesFromKostenStelle(kostenstelle);
    }

    @Override
    public BenutzerIbos findBenutzerByBnadnr(Integer bnadrn) {
        return benutzerIbosRepository.findBenutzerByBnadnr(bnadrn);
    }

    private Object[] findForecastRevenue(Integer projectNumber) {
        List<Object[]> result = benutzerIbosRepository.findForecastRevenue(projectNumber);
        if(result != null && !result.isEmpty()) {
            return result.get(0);
        }
        return null;
    }

    private void setForecastDataAndDeviations(ProjectForecastDataDto projectForecastDataDto, ProjectRevenueDataDto projectRevenueDataDto) {
        if(projectRevenueDataDto.getPlanStunden() != null && projectRevenueDataDto.getIstStunden() != null) {
            projectForecastDataDto.setForecastStunden(projectRevenueDataDto.getPlanStunden().add(projectRevenueDataDto.getIstStunden()));
            if(projectRevenueDataDto.getStundenSatz() != null) {
                projectForecastDataDto.setForecastUmsatz((projectRevenueDataDto.getPlanStunden().add(projectRevenueDataDto.getIstStunden())).multiply(projectRevenueDataDto.getStundenSatz()));
            }
        }
        if(projectForecastDataDto.getPlanUmsatz() != null && projectForecastDataDto.getSollUmsatz() != null) {
            projectForecastDataDto.setAbweichungSollPlan(projectForecastDataDto.getPlanUmsatz().subtract(projectForecastDataDto.getSollUmsatz()));
        }
        if(projectForecastDataDto.getForecastUmsatz() != null && projectForecastDataDto.getSollUmsatz() != null) {
            projectForecastDataDto.setAbweichungSollForecast(projectForecastDataDto.getForecastUmsatz().subtract(projectForecastDataDto.getSollUmsatz()));
        }
        if(projectForecastDataDto.getForecastUmsatz() != null && projectForecastDataDto.getPlanUmsatz() != null) {
            projectForecastDataDto.setAbweichungPlanForecast(projectForecastDataDto.getForecastUmsatz().subtract(projectForecastDataDto.getPlanUmsatz()));
        }
    }


    private ProjectRevenueDataDto findRevenueAndHoursFromProjectStartToToday(Integer projectNumber) {
        Object[] isSollData = findIsAndSollRevenueBetweenDates(projectNumber, LocalDate.now().minusYears(100), LocalDate.now());
        ProjectRevenueDataDto projectRevenueDataDto = mapDataToProjectRevenueDataDto(isSollData);
        return calculateRevenueAndHoursForProject(projectNumber, projectRevenueDataDto, LocalDate.now().minusYears(100), LocalDate.now());
    }

    private ProjectRevenueDataDto findRevenueAndHoursFromTodayToProjectEnd(Integer projectNumber) {
        LocalDate bis = benutzerIbosRepository.findProjectSeminarGreatestEndDate(projectNumber);
        Object[] isSollData = findIsAndSollRevenueBetweenDates(projectNumber, LocalDate.now(), bis);
        ProjectRevenueDataDto projectRevenueDataDto = mapDataToProjectRevenueDataDto(isSollData);
        return calculateRevenueAndHoursForProject(projectNumber, projectRevenueDataDto, LocalDate.now(), bis);
    }

    private Object[] findIsAndSollRevenueBetweenDates(Integer projectNumber, LocalDate von, LocalDate bis) {
/*        if(von.equals(LocalDate.now())) {
            von = LocalDate.now().minusDays(1);
        }*/
        List<Object[]> result = benutzerIbosRepository.findIsAndSollRevenueBetweenDates(projectNumber, von, bis);
        if(result != null && !result.isEmpty()) {
            return result.get(0);
        }
        return null;
    }

    private ProjectRevenueDataDto calculateRevenueAndHoursForProject(Integer projectNumber, ProjectRevenueDataDto projectRevenueDataDto, LocalDate von, LocalDate bis) {
/*        if(von.equals(LocalDate.now())) {
            von = LocalDate.now().plusDays(1);
        }*/

        Object[] planData = findPlanRevenueBetweenDates(projectNumber, von, bis);
        setPlanDataAndDeviations(planData, projectRevenueDataDto);
        return projectRevenueDataDto;
    }

    private Object[] findPlanRevenueBetweenDates(Integer projectNumber, LocalDate von, LocalDate bis) {
        List<Object[]> result = benutzerIbosRepository.findPlanRevenueBetweenDates(projectNumber, von, bis);
        if(result != null && !result.isEmpty()) {
            return result.get(0);
        }
        return null;
    }

    private void setPlanDataAndDeviations(Object[] planData, ProjectRevenueDataDto projectRevenueDataDto) {
        if(planData != null) {
            projectRevenueDataDto.setPlanStunden((BigDecimal) planData[0]);
            projectRevenueDataDto.setPlanUmsatz((BigDecimal) planData[1]);
            if(projectRevenueDataDto.getSollStunden() == null) {
                projectRevenueDataDto.setSollStunden((BigDecimal) planData[2]);
            }
            if(projectRevenueDataDto.getSollUmsatz() == null) {
                projectRevenueDataDto.setSollUmsatz((BigDecimal) planData[3]);
            }
        }
        calculateDeviations(projectRevenueDataDto);
    }

    private void calculateDeviations(ProjectRevenueDataDto data) {
        if(data.getPlanUmsatz() != null && data.getSollUmsatz() != null) {
            data.setAbweichungSollPlan(data.getPlanUmsatz().subtract(data.getSollUmsatz()));
        }
        if(data.getIstUmsatz() != null && data.getSollUmsatz() != null) {
            data.setAbweichungSollIst(data.getIstUmsatz().subtract(data.getSollUmsatz()));
        }
        if(data.getIstUmsatz() != null && data.getPlanUmsatz() != null) {
            data.setAbweichungPlanIst(data.getIstUmsatz().subtract(data.getPlanUmsatz()));
        }
    }

    private BasicProjectDto mapBasicProjectDto(Object[] row) {
        return new BasicProjectDto(
                (Integer) row[0],
                (String) row[1]);
    }

    private ProjectRevenueDataDto mapDataToProjectRevenueDataDto(Object[] data) {
        ProjectRevenueDataDto projectRevenueDataDto = new ProjectRevenueDataDto();
        if(data == null) {
            return projectRevenueDataDto;
        }
        projectRevenueDataDto.setProjectNumber((Integer) data[0]);
        projectRevenueDataDto.setVon(mapLocalDateRow(data[1]));
        projectRevenueDataDto.setBis(mapLocalDateRow(data[2]));
        projectRevenueDataDto.setSollUmsatz((BigDecimal) data[3]);
        projectRevenueDataDto.setSollStunden((BigDecimal) data[4]);
        projectRevenueDataDto.setIstStunden((BigDecimal) data[5]);
        projectRevenueDataDto.setIstUmsatz((BigDecimal) data[6]);
        projectRevenueDataDto.setStundenSatz((BigDecimal) data[7]);
        return projectRevenueDataDto;
    }

    private ProjectForecastDataDto mapDataToForecastDataDto(Object[] data) {
        ProjectForecastDataDto projectForecastDataDto = new ProjectForecastDataDto();
        if(data == null) {
            return projectForecastDataDto;
        }
        projectForecastDataDto.setSollUmsatz((BigDecimal) data[0]);
        projectForecastDataDto.setSollStunden((BigDecimal) data[1]);
        projectForecastDataDto.setPlanStunden((BigDecimal) data[2]);
        projectForecastDataDto.setPlanUmsatz((BigDecimal) data[3]);
        return projectForecastDataDto;
    }

    private BasicPersonDto mapDataToBasicPersonDto(Object[] data) {
        BasicPersonDto basicPersonDto = new BasicPersonDto();
        if(data == null) {
            return basicPersonDto;
        }
        basicPersonDto.setName((String) data[0]);
        basicPersonDto.setId((Long) data[1]);
        return basicPersonDto;
    }
}
