package com.ibosng.dbservice.services.impl.moxis;

import com.ibosng.dbservice.entities.moxis.Invitee;
import com.ibosng.dbservice.repositories.moxis.InviteeRepository;
import com.ibosng.dbservice.services.moxis.InviteeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InviteeServiceImpl implements InviteeService {

    private final InviteeRepository inviteeRepository;

    public InviteeServiceImpl(InviteeRepository inviteeRepository) {
        this.inviteeRepository = inviteeRepository;
    }

    @Override
    public List<Invitee> findAll() {
        return inviteeRepository.findAll();
    }

    @Override
    public Optional<Invitee> findById(Integer id) {
        return inviteeRepository.findById(id);
    }

    @Override
    public Invitee save(Invitee object) {
        return inviteeRepository.save(object);
    }

    @Override
    public List<Invitee> saveAll(List<Invitee> objects) {
        return inviteeRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        inviteeRepository.deleteById(id);
    }

    @Override
    public List<Invitee> findAllByIdentifier(String identifier) {
        return null;
    }
}
