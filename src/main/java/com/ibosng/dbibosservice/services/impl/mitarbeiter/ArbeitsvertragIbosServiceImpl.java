package com.ibosng.dbibosservice.services.impl.mitarbeiter;

import com.ibosng.dbibosservice.dtos.ContractDto;
import com.ibosng.dbibosservice.dtos.DienstortDto;
import com.ibosng.dbibosservice.entities.mitarbeiter.ArbeitsvertragIbos;
import com.ibosng.dbibosservice.repositories.mitarbeiter.ArbeitsvertragIbosRepository;
import com.ibosng.dbibosservice.services.mitarbeiter.ArbeitsvertragIbosService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ibosng.dbibosservice.utils.Parsers.mapLocalDateRow;

@Service
public class ArbeitsvertragIbosServiceImpl implements ArbeitsvertragIbosService {

    private final ArbeitsvertragIbosRepository arbeitsvertragIbosRepository;

    public ArbeitsvertragIbosServiceImpl(ArbeitsvertragIbosRepository arbeitsvertragIbosRepository) {
        this.arbeitsvertragIbosRepository = arbeitsvertragIbosRepository;
    }

    @Override
    public List<ArbeitsvertragIbos> findAll() {
        return arbeitsvertragIbosRepository.findAll();
    }

    @Override
    public Optional<ArbeitsvertragIbos> findById(Integer id) {
        return arbeitsvertragIbosRepository.findById(id);
    }

    @Override
    public ArbeitsvertragIbos save(ArbeitsvertragIbos object) {
        return arbeitsvertragIbosRepository.save(object);
    }

    @Override
    public List<ArbeitsvertragIbos> saveAll(List<ArbeitsvertragIbos> objects) {
        return arbeitsvertragIbosRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        arbeitsvertragIbosRepository.deleteById(id);
    }

    @Override
    public List<ContractDto> getAllContracts(String user) {
        List<ContractDto> allContracts = getContracts(user);
        allContracts.addAll(getContractsWithoutLeistungen(user));
        allContracts.sort(Comparator.comparing(ContractDto::getDatumVon));
        return allContracts;
    }

    @Override
    public List<ContractDto> getContracts(String user) {
        List<Object[]> rawData = arbeitsvertragIbosRepository.getContractsDataRaw(user);
        return rawData.stream()
                .map(this::mapContractDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ContractDto> getContractsWithoutLeistungen(String user) {
        List<Object[]> rawData = arbeitsvertragIbosRepository.getContractsWithoutLeistungen(user);
        return rawData.stream()
                .map(this::mapContractDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAllDienstort() {
        return arbeitsvertragIbosRepository.getAllDienstort();
    }

    @Override
    public List<String> getAllKonstenstellen() {
        return arbeitsvertragIbosRepository.getAllKonstenstellen();
    }

    @Override
    public List<DienstortDto> findDienstortByBezeichnung(String bezeichnung) {
        List<DienstortDto> dienstortDtos = new ArrayList<>();
        List<Object[]> objects = arbeitsvertragIbosRepository.findDienstortByBezeichnung(bezeichnung);
        objects.forEach(object -> dienstortDtos.add(mapDienstortDto(object)));
        return dienstortDtos;
    }

    @Override
    public List<ArbeitsvertragIbos> findAllByAddressNo(Integer addreseId) {
        return arbeitsvertragIbosRepository.findAllByAddressNo(addreseId);
    }

    @Override
    public List<ArbeitsvertragIbos> findAllByChangedAfterAndAderuser(String createdBy, LocalDateTime after) {
        return arbeitsvertragIbosRepository.findAllByEruserAndErdaAfterOrEruserAndAedaAfter(createdBy, after, createdBy, after);
    }

    private ContractDto mapContractDto(Object[] row) {
        return new ContractDto(
                mapLocalDateRow(row[0]),
                mapLocalDateRow(row[1]),
                (String) row[2],
                (String) row[3],
                (String) row[4],
                (Long) row[5],
                (String) row[6],
                mapLocalDateRow(row[7]),
                (BigDecimal) row[8],
                (String) row[9],
                (String) row[10],
                (String) row[11]);
    }

    private DienstortDto mapDienstortDto(Object[] row) {
        return new DienstortDto(
                (Long) row[0],
                (String) row[1],
                (String) row[4],
                (String) row[5],
                (String) row[6],
                (String) row[7],
                (String) row[8],
                (String) row[10]);
    }
}
