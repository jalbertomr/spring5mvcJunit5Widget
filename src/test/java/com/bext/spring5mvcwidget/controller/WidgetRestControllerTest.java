package com.bext.spring5mvcwidget.controller;

import com.bext.spring5mvcwidget.model.Widget;
import com.bext.spring5mvcwidget.service.WidgetServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class WidgetRestControllerTest {

    @MockBean
    private WidgetServiceImpl widgetService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getWidget() {
    }

    @Test
    @DisplayName("GET /widgets success")
    void getWidgetsSuccess() throws Exception {
        // setup mock service
        Widget widget1 = new Widget(1L, "Name1", "Description1", 1);
        Widget widget2 = new Widget(2L, "Name2", "Description2", 4);
        doReturn(Lists.newArrayList(widget1, widget2)).when(widgetService).findAll();
        // execute GET request
        mockMvc.perform(get("/rest/widgets"))
                // validate response code and content Type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // validate headers
                .andExpect(header().string(HttpHeaders.LOCATION, "/rest/widgets"))
                // validate the returned fields
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Name1")))
                .andExpect(jsonPath("$[0].description", is("Description1")))
                .andExpect(jsonPath("$[0].version", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Name2")))
                .andExpect(jsonPath("$[1].description", is("Description2")))
                .andExpect(jsonPath("$[1].version", is(4))
                );
    }

    @Test
    @DisplayName("GET /rest/widget/{id}")
    void getWidgetById() throws Exception {
        //setup mock service
        Widget widget = new Widget(1L, "name1", "Description1", 2);
        doReturn(Optional.of(widget)).when(widgetService).findById(1L);
        // execute GET request
        mockMvc.perform(get("/rest/widget/1"))
                // validate response code and content Type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // validate headers
                .andExpect(header().string(HttpHeaders.LOCATION, "/rest/widget/1"))
                .andExpect(header().string(HttpHeaders.ETAG, "\"2\""))
                // validate the returned fields
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("name1")))
                .andExpect(jsonPath("$.description", is("Description1")))
                .andExpect(jsonPath("$.version", is(2)));
    }

    @Test
    @DisplayName("GET /rest/widget/{id} NotFound")
    void getWidgetByIdNotFound() throws Exception {
        // setup mock service
        doReturn(Optional.empty()).when(widgetService).findById(1L);
        // execute GET request
        mockMvc.perform(get("/rest/widget/1"))
                // validate response code
                .andExpect(status().isNotFound());
        // validate the return fields
    }

    @Test
    @DisplayName("POST /rest/widget")
    void addWidget() throws Exception {
        // setup mock service
        Widget widgetPost = new Widget("name1", "Description1");
        Widget widgetReturn = new Widget(1L, "name1", "Description1", 1);
        doReturn(widgetReturn).when(widgetService).save(any());
        // execute POST request with content type and body
        mockMvc.perform(post("/rest/widget")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(widgetPost)))
                // validate response code
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // validate headers
                .andExpect(header().string(HttpHeaders.LOCATION, "/rest/widget/1"))
                .andExpect(header().string(HttpHeaders.ETAG, "\"1\""))
                // validate return fields
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("name1")))
                .andExpect(jsonPath("$.description", is("Description1")))
                .andExpect(jsonPath("$.version", is(1)));
    }


    @Test
    @DisplayName("PUT /rest/widget/{id} Success")
    void updateWidgetSuccess() throws Exception {
        // setup mock service
        Widget widgetPut = new Widget(1L, "namePut", "DescriptionPut", 1);
        Widget widgetReturn = new Widget(1L, "namePut", "DescriptionPut", 2);
        doReturn(Optional.of(widgetReturn)).when(widgetService).findById(1L);
        doReturn(widgetReturn).when(widgetService).save(any());
        // execute PUT request with header IF-MATCH
        mockMvc.perform(put("/rest/widget/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.IF_MATCH, 2)
                .content(asJsonString(widgetPut)))
                // validate response code
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // validate headers
                .andExpect(header().string(HttpHeaders.LOCATION, "/rest/widget/1"))
                .andExpect(header().string(HttpHeaders.ETAG, "\"2\""))
                // validate return fields
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("namePut")))
                .andExpect(jsonPath("$.description", is("DescriptionPut")))
                .andExpect(jsonPath("$.version", is(2)));
    }

    @Test
    @DisplayName("PUT /rest/widget/{id} Conflict")
    void updateWidgetConflict() throws Exception {
        // setup mock service
        Widget widgetPut = new Widget(1L, "namePut", "descriptionPut", 1);
        Widget widgetReturn = new Widget(1L, "namePut", "descriptionPut", 2);
        doReturn(Optional.of(widgetReturn)).when(widgetService).findById(1L);
        // execute PUT request with header IF-MATCH not equal, with headers
        mockMvc.perform(put("/rest/widget/1")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.IF_MATCH, 10)
                .content(asJsonString(widgetPut)))
                // validate response code
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("PUT /rest/widget/{id} NotFound")
    void updateWidgetNotFound() throws Exception {
        // setup mock service
        Widget widgetPut = new Widget(1L, "namePut", "descriptionPut", 1);
        Widget widgetReturn = new Widget(1L, "namePut", "descriptionPut", 2);
        doReturn(Optional.empty()).when(widgetService).findById(1L);
        // execute PUT request
        mockMvc.perform(put("/rest/widget/1")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.IF_MATCH, 2)
                .content(asJsonString(widgetPut)))
                // validate response code
                .andExpect(status().isNotFound());
    }

    @Test
    void updateWidgetProper() {
    }

    @Test
    void deleteWidget() {
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}