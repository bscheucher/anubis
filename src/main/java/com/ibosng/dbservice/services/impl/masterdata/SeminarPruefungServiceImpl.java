package com.ibosng.dbservice.services.impl.masterdata;


import com.ibosng.dbservice.dtos.SeminarPruefungDto;
import com.ibosng.dbservice.entities.seminar.SeminarPruefung;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer2Seminar;
import com.ibosng.dbservice.repositories.masterdata.Teilnehmer2SeminarRepository;
import com.ibosng.dbservice.repositories.seminar.SeminarPruefungRepository;
import com.ibosng.dbservice.services.SeminarPruefungService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class SeminarPruefungServiceImpl implements SeminarPruefungService {

    private final SeminarPruefungRepository seminarPruefungRepository;
    private final Teilnehmer2SeminarRepository teilnehmer2SeminarRepository;

    public SeminarPruefungServiceImpl(SeminarPruefungRepository seminarPruefungRepository,
                                      Teilnehmer2SeminarRepository teilnehmer2SeminarRepository) {
        this.seminarPruefungRepository = seminarPruefungRepository;
        this.teilnehmer2SeminarRepository = teilnehmer2SeminarRepository;
    }

    @Override
    public List<SeminarPruefung> findAll() {
        return seminarPruefungRepository.findAll();
    }

    @Override
    public Optional<SeminarPruefung> findById(Integer id) {
        return seminarPruefungRepository.findById(id);
    }

    @Override
    public SeminarPruefung save(SeminarPruefung object) {
        return seminarPruefungRepository.save(object);
    }

    @Override
    public List<SeminarPruefung> saveAll(List<SeminarPruefung> objects) {
        return seminarPruefungRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        seminarPruefungRepository.deleteById(id);
    }

    @Override
    public List<SeminarPruefung> findAllByIdentifier(String identifier) {
        return null;
    }

    public List<SeminarPruefung> findAllByTeilnehmerIdAndSeminarId(Integer teilnehmerId, Integer seminarId) {
        Optional<Teilnehmer2Seminar> t2s = teilnehmer2SeminarRepository.findByTeilnehmer_IdAndSeminar_Id(teilnehmerId, seminarId);
        if (t2s.isPresent()) {
            return seminarPruefungRepository.findByTeilnehmer2Seminar_Id(t2s.get().getId());
        } else {
            return Collections.emptyList();
        }
    }

    public SeminarPruefungDto mapSeminarPruefungToDto(SeminarPruefung seminarPruefung) {
        SeminarPruefungDto seminarPruefungDto = new SeminarPruefungDto();
        if (seminarPruefung != null) {
            seminarPruefungDto.setId(seminarPruefung.getId());
            seminarPruefungDto.setBezeichnung(seminarPruefung.getBezeichnung() != null ? seminarPruefung.getBezeichnung().getName() : null);
            seminarPruefungDto.setPruefungArt(seminarPruefung.getPruefungArt() != null ? seminarPruefung.getPruefungArt().getName() : null);
            seminarPruefungDto.setGegenstand(seminarPruefung.getGegenstand() != null ? seminarPruefung.getGegenstand().getName() : null);
            seminarPruefungDto.setNiveau(seminarPruefung.getNiveau() != null ? seminarPruefung.getNiveau().getName() : null);
            seminarPruefungDto.setInstitut(seminarPruefung.getInstitut() != null ? seminarPruefung.getInstitut().getName() : null);
            seminarPruefungDto.setAntritt(seminarPruefung.getAntritt());
            seminarPruefungDto.setBegruendung(seminarPruefung.getBegruendung() != null ? seminarPruefung.getBegruendung().getName() : null);
            seminarPruefungDto.setErgebnis(seminarPruefung.getErgebnisType() != null ? seminarPruefung.getErgebnisType().getName() : null);
            seminarPruefungDto.setErgebnisInProzent(seminarPruefung.getErgebnisInProzent() != null ? seminarPruefung.getErgebnisInProzent().toString() : null);
            seminarPruefungDto.setPruefungAm(seminarPruefung.getPruefungAm() != null ? seminarPruefung.getPruefungAm().toString() : null);
        }
        return seminarPruefungDto;
    }
}
