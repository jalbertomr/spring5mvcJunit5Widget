package com.bext.spring5mvcwidget.repository;

import com.bext.spring5mvcwidget.model.Widget;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WidgetRepository extends CrudRepository<Widget, Long> {
    @Query("SELECT w FROM Widget w WHERE w.name LIKE ?1")
    List<Widget> findWidgetWithNameLike(String name);
}
