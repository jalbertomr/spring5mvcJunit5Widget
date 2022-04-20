package com.bext.spring5mvcwidget.repository;

import com.bext.spring5mvcwidget.model.Widget;
import org.springframework.data.repository.CrudRepository;

public interface WidgetRepository extends CrudRepository<Widget, Long> {
}
