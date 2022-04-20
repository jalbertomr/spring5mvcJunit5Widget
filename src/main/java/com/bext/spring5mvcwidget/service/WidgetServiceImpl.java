package com.bext.spring5mvcwidget.service;

import com.bext.spring5mvcwidget.model.Widget;
import com.bext.spring5mvcwidget.repository.WidgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WidgetServiceImpl implements IWidgetService {

    @Autowired
    private WidgetRepository widgetRepository;

    public Optional<Widget> findById(Long id){
        return widgetRepository.findById(id);
    }

    public Iterable<Widget> findAll(){
        return widgetRepository.findAll();
    }

    public void save(Widget widget){
        widgetRepository.save(widget);
    }

    public void deleteById( Long id){
        widgetRepository.deleteById(id);
    }
}

