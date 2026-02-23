package com.ibosng.gatewayservice.services;

import com.ibosng.dbservice.dtos.mitarbeiter.StammdatenDto;
import com.ibosng.dbservice.dtos.mitarbeiter.UnterhaltsberechtigteDto;
import com.ibosng.dbservice.dtos.mitarbeiter.VertragsdatenDto;
import com.ibosng.dbservice.dtos.mitarbeiter.VordienstzeitenDto;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterType;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.dbservice.entities.workflows.WWorkflow;
import com.ibosng.gatewayservice.dtos.response.PayloadTypeList;

public interface GenericMAService {
    boolean isVertragsdatenWithoutError(Vertragsdaten vertragsdaten);

    void manageKVCalculation(String personalnummer, WWorkflow workflow, String changedBy, boolean isOnboarding);

    PayloadTypeList<VordienstzeitenDto> createPayloadForMultipleVordienstzeiten(Vertragsdaten vertragsdaten);

    PayloadTypeList<UnterhaltsberechtigteDto> createPayloadForMultipleUnterhaltsberechtigte(Vertragsdaten vertragsdaten);

    PayloadTypeList<VordienstzeitenDto> createPayloadForSingleVordienstzeiten(VordienstzeitenDto vordienstzeitenDto);

    PayloadTypeList<UnterhaltsberechtigteDto> createPayloadForSingleUnterhaltsberechtigte(UnterhaltsberechtigteDto unterhaltsberechtigteDto);

    Vertragsdaten getVertragsdatenEntity(String personalnummer, Boolean isOnboarding);

    PayloadTypeList<StammdatenDto> createPayloadForStammdaten(StammdatenDto stammdatenDto);

    WWorkflow getWorkflowForMaType(Personalnummer personalnummer);

    PayloadTypeList<MitarbeiterType> createPayloadForMAType(MitarbeiterType mitarbeiterType);

    PayloadTypeList<VertragsdatenDto> createPayloadForVertragsdaten(VertragsdatenDto vertragsDatenDto, boolean isMitarbeiter);

    void updateTeilnehmerStammdaten(Stammdaten stammdaten, String changedBy);

    boolean isVertragsdatenWithoutError(String personalnummer, boolean isOnboarding);

    Stammdaten getStammdatenEntity(String personalnummer, Boolean isOnboarding);

    boolean isStammdatenWithoutError(String personalnummer, boolean isOnboarding);

    PayloadTypeList<VordienstzeitenDto> createPayloadForMultipleVordienstzeiten(String personalnummer, boolean isOnboarding);

    PayloadTypeList<UnterhaltsberechtigteDto> createPayloadForMultipleUnterhaltsberechtigte(String personalnummer, boolean isOnboarding);
}
