package com.ibosng.dbservice.services.impl.zeiterfassung;

import com.ibosng.dbservice.entities.zeiterfassung.ZeiterfassungReason;
import com.ibosng.dbservice.repositories.zeiterfassung.ZeiterfassungReasonRepository;
import com.ibosng.dbservice.services.zeiterfassung.ZeiterfassungReasonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ZeiterfassungReasonServiceImpl implements ZeiterfassungReasonService {
    private final ZeiterfassungReasonRepository zeiterfassungReasonRepository;

    @Override
    public List<ZeiterfassungReason> findAll() {
        return zeiterfassungReasonRepository.findAll();
    }

    @Override
    public Optional<ZeiterfassungReason> findById(Integer id) {
        return zeiterfassungReasonRepository.findById(id);
    }

    @Override
    public ZeiterfassungReason save(ZeiterfassungReason object) {
        return zeiterfassungReasonRepository.save(object);
    }

    @Override
    public List<ZeiterfassungReason> saveAll(List<ZeiterfassungReason> objects) {
        return zeiterfassungReasonRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        zeiterfassungReasonRepository.deleteById(id);
    }

    @Override
    public List<ZeiterfassungReason> findAllByIdentifier(String identifier) {
        return List.of();
    }

    @Override
    public ZeiterfassungReason findByBezeichnungAndShortBezeichnung(String bezeichnung, String shortBezeichnung) {
        return zeiterfassungReasonRepository.findByBezeichnungIgnoreCaseAndShortBezeichnungIgnoreCase(bezeichnung, shortBezeichnung)
                .orElse(null);
    }

    @Override
    public ZeiterfassungReason findByBezeichnung(String bezeichnung) {
        return zeiterfassungReasonRepository.findByBezeichnung(bezeichnung);
    }

    @Override
    public List<ZeiterfassungReason> findAllByIbosId(Integer ibosId) {
        return zeiterfassungReasonRepository.findAllByIbosId(ibosId);
    }

    @Override
    public List<ZeiterfassungReason> findAllByIbosIdAndBezeichnung(Integer ibosId, String bezeichnung) {
        return zeiterfassungReasonRepository.findAllByIbosIdAndBezeichnung(ibosId, bezeichnung);
    }

    @Override
    public List<ZeiterfassungReason> findAllByIbosIdAndShortBezeichnung(Integer ibosId, String shortBezeichnung) {
        return zeiterfassungReasonRepository.findAllByIbosIdAndShortBezeichnung(ibosId, shortBezeichnung);
    }

    @Override
    public List<Integer> findAllReasonsWithIbosidNotNull() {
        return zeiterfassungReasonRepository.findAllReasonsWithIbosidNotNull();
    }
}
