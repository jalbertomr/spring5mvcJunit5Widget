package com.bext.spring5mvcwidget.repository;

import com.bext.spring5mvcwidget.model.Widget;
import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.DBUnitExtension;
import org.assertj.core.util.Lists;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(DBUnitExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class WidgetRepositoryTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private WidgetRepository widgetRepository;

    @Test
    @DataSet("widgets.yml")
    void findAll(){
        ArrayList<Widget> widgets = Lists.newArrayList(widgetRepository.findAll());
        Assertions.assertEquals(2, widgets.size(),"Widgets size should be 2");
    }

    @Test
    @DataSet("widgets.yml")
    void findByIdSuccess(){
        Optional<Widget> widgetFound = widgetRepository.findById(1L);
        Assertions.assertTrue(widgetFound.isPresent(), "Widget with id 1 should exist");

        Widget widget = widgetFound.get();
        Assertions.assertEquals( 1, widget.getId(), "Widget id should be 1");
        Assertions.assertEquals( "Widget 1" , widget.getName(), "Widget name should be Widget 1");
        Assertions.assertEquals("Description Widget 1", widget.getDescription(), "Widget Description  should be Description Widget 1");
        Assertions.assertEquals( 1, widget.getVersion(), "Widget version should be 1");
    }

    @Test
    @DataSet("widgets.yml")
    void findByIdNotFound() {
        Optional<Widget> widgetFound = widgetRepository.findById(3L);
        Assertions.assertEquals(Optional.empty(), widgetFound, "Widget with id 3 should not be found");
    }

    @Test
    @DataSet("widgets.yml")
    void findWidgetWithNameLike(){
        List<Widget> widgetWithNameLike = widgetRepository.findWidgetWithNameLike("Widget%");
        Assertions.assertEquals(2, widgetWithNameLike.size(), "Widgets size should be 2");
    }
}