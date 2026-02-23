package com.ibosng.dbservice.services.impl;

import com.ibosng.dbservice.entities.Widget;
import com.ibosng.dbservice.repositories.WidgetRepository;
import com.ibosng.dbservice.services.WidgetService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WidgetServiceImpl implements WidgetService {

    private final WidgetRepository widgetRepository;

    public WidgetServiceImpl(WidgetRepository widgetRepository) {
        this.widgetRepository = widgetRepository;
    }

    @Override
    public List<Widget> findAll() {
        return widgetRepository.findAll();
    }

    @Override
    public Optional<Widget> findById(Integer id) {
        return widgetRepository.findById(id);
    }

    @Override
    public Widget save(Widget object) {
        return widgetRepository.save(object);
    }

    @Override
    public List<Widget> saveAll(List<Widget> objects) {
        return widgetRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        widgetRepository.deleteById(id);
    }

    @Override
    public List<Widget> findAllByIdentifier(String identifier) {
        return null;
    }
}
