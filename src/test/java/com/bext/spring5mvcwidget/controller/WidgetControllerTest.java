package com.bext.spring5mvcwidget.controller;

import com.bext.spring5mvcwidget.model.Widget;
import com.bext.spring5mvcwidget.service.WidgetServiceImpl;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class WidgetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WidgetServiceImpl widgetService;

    @Test
    @DisplayName("load new widget page /widget/new")
    void newWidget() throws Exception {
        // execute GET Request
        mockMvc.perform(get("/widget/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("widgetform"));
    }

    @Test
    @DisplayName("POST /widget create new widget page")
    void createWidget() throws Exception {
        // execute POST request
        mockMvc.perform(post("/widget"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("GET /widget view")
    void getWidgetById() throws Exception {
        //setup mock service
        doReturn( Optional.of(new Widget(1L, "name1","description",1))).when(widgetService).findById(1L);
        // execute GET request
        mockMvc.perform(get("/widget/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("widget"));
    }

    @Test
    @DisplayName("GET /widgets view")
    void getWidgets() throws Exception {
        //setup mock service
        Widget widget1 = new Widget(1L, "name1", "description1", 1);
        Widget widget2 = new Widget(2L, "name2", "description2", 1);
        doReturn(Lists.newArrayList(widget1,widget2)).when(widgetService).findAll();
        // execute GET Request
        mockMvc.perform(get("/widgets"))
                .andExpect(status().isOk())
                .andExpect(view().name("widgets"));
    }

    @Test
    @DisplayName("GET /widgets/edit/{id} view")
    void editWidget() throws Exception {
        // execute GET Request
        mockMvc.perform(get("/widget/edit/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("widgetform"));
    }

    @Test
    @DisplayName("Update Widget by Id POST /widget/id")
    void updateWidget() throws Exception {
      mockMvc.perform(post("/widget/{id}", 1))
              .andExpect(status().is3xxRedirection())
              .andExpect(view().name("redirect:/widget/1"));
    }

    @Test
    @DisplayName("GET /widget/delete/{id} view")
    void deleteWidget() throws Exception {
        doNothing().when(widgetService).deleteById(1L);

        mockMvc.perform(get("/widget/delete/{id}", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/widgets/"));
    }
}