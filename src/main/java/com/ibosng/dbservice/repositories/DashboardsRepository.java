package com.ibosng.dbservice.repositories;

import com.ibosng.dbservice.entities.Dashboards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional("postgresTransactionManager")
public interface DashboardsRepository extends JpaRepository<Dashboards, Integer> {

    String DELETE_BY_ID = "delete from Dashboards dash where dash.id = :id";

    String FIND_ALL_IDS_BY_BENUTZER_ID = "select dash.id from Dashboards dash where dash.benutzer.azureId = :azureId";

    String FIND_ALL_BY_BENUTZER_ID = "select dash from Dashboards dash where dash.benutzer.azureId = :azureId";

    String FIND_BY_ID_AND_USER_AZURE_ID = "select dash from Dashboards dash where dash.benutzer.azureId = :azureId and dash.id = :dashboardId";

    @Modifying
    @Query(value = DELETE_BY_ID)
    void deleteById(Integer id);

    @Query(value = FIND_ALL_IDS_BY_BENUTZER_ID)
    List<Integer> getAllDashboardsIdsByAzureId(String azureId);

    @Query(value = FIND_ALL_BY_BENUTZER_ID)
    List<Dashboards> getAllDashboardsByAzureId(String azureId);

    @Query(value = FIND_BY_ID_AND_USER_AZURE_ID)
    Dashboards findDashboardByIdAndUserAzureId(Integer dashboardId, String azureId);
}
