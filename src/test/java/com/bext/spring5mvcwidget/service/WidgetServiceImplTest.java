package com.bext.spring5mvcwidget.service;

import com.bext.spring5mvcwidget.model.Widget;
import com.bext.spring5mvcwidget.repository.WidgetRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
class WidgetServiceImplTest {

    @Autowired
    private WidgetServiceImpl widgetService;

    @MockBean
    private WidgetRepository mockWidgetRepository;

    @Test
    @DisplayName("Test findById Success")
    void findById() {
        // setup the mocked Repository
        Widget widget = new Widget(1L, "Widget Name", "Description mocked repo");
        doReturn(Optional.of(widget)).when(mockWidgetRepository).findById(1L);

        // Service call
        Optional<Widget> returnedWidget = widgetService.findById(1L);

        // Assert Response
        Assertions.assertTrue( returnedWidget.isPresent(), "Widget not Found");
        Assertions.assertSame( returnedWidget.get(), widget, "Widget returned not the same as one mocked");
    }

    @Test
    @DisplayName("Test findById when Not Found")
    void findByIdNotFound(){
        // setup the mocked Repository when not Found
        doReturn(Optional.empty()).when(mockWidgetRepository).findById(1L);

        //Service call
        Optional<Widget> returnedWidget = widgetService.findById(1L);

        // Assert Response
        Assertions.assertFalse( returnedWidget.isPresent(),"Widget should not be found");
    }

    @Test
    @DisplayName("Test findAll")
    void findAll() {
        //setup the mocked repository
        Widget widget1 = new Widget(1L, "name 1","Description 1");
        Widget widget2 = new Widget(2L, "name 2","Description 2");
        List<Widget> widgets = Arrays.asList(widget1, widget2);
        doReturn(widgets).when(mockWidgetRepository).findAll();

        // Service call
        List<Widget> returnedWidgets = (List<Widget>) widgetService.findAll();

        // Assert Response
        Assertions.assertEquals(2, returnedWidgets.size(), "findAll should return 2");
    }

    @Test
    @DisplayName("Test save")
    void save() {
        // setup the mocked repository
        Widget widget = new Widget(1L, "name","description");
        Widget widgetReturn = widget;
        widgetReturn.setVersion( widget.getVersion() + 1);
        doReturn(widgetReturn).when(mockWidgetRepository).save(any());

        // service call
        Widget savedWidget = widgetService.save(widget);

        // Assert Response
        Assertions.assertNotNull( widget, "Widget should not be null");
        Assertions.assertEquals(2, savedWidget.getVersion(),"Widget Version should be 2");

    }

    @Test
    void deleteById() {
    }
}