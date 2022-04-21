package com.bext.spring5mvcwidget.service;

import com.bext.spring5mvcwidget.model.Widget;
import com.bext.spring5mvcwidget.repository.WidgetRepository;

import java.util.Optional;

public interface IWidgetService {
    public Optional<Widget> findById(Long id);
    public Iterable<Widget> findAll();
    public Widget save(Widget widget);
    public void deleteById( Long id);
}
