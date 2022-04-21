package com.bext.spring5mvcwidget.controller;


import com.bext.spring5mvcwidget.model.Widget;
import com.bext.spring5mvcwidget.service.WidgetServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@RestController
public class WidgetRestController {
    private final static Logger logger = LogManager.getLogger(WidgetRestController.class);

    @Autowired
    private WidgetServiceImpl widgetService;

    @GetMapping("/rest/widget/{id}")
    public ResponseEntity<?> getWidget(@PathVariable Long id){
        return  widgetService.findById(id)
                .map(widget -> {
                    try {
                        return ResponseEntity.ok()
                                .eTag(Integer.toString(widget.getVersion()))
                                .location(new URI("/rest/widget/" + widget.getId()))
                                .body(widget);
                    } catch (URISyntaxException e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/rest/widgets")
    public ResponseEntity<?> getWidgets() {
        try {
            return ResponseEntity.ok()
                    .location(new URI("/rest/widgets"))
                    .body( widgetService.findAll());
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/rest/widget")
    public ResponseEntity<?> addWidget(@RequestBody Widget widget){
        logger.info("request widget: {} {}", widget.getName(), widget.getDescription());
        Widget savedWidget = widgetService.save(widget);

        try {
            return ResponseEntity.created(new URI("/rest/widget/" + savedWidget.getId()))
                    .eTag( Integer.toString(savedWidget.getVersion()))
                    .body( savedWidget);
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/rest/widget/{id}")
    public ResponseEntity<?> updateWidget(@RequestBody Widget widget, @PathVariable Long id, @RequestHeader(HttpHeaders.IF_MATCH) String ifMatch){
        // Get widget with id
        Optional<Widget> widgetFound = widgetService.findById(id);
        if (widgetFound.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        // Validate that the if-match header matches the widget's header
        if (!ifMatch.equalsIgnoreCase(Integer.toString(widgetFound.get().getVersion()))) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Header IF-MATCH");
        }
        // update the widget
        widget.setId(id);
        Widget saveWidget = widgetService.save(widget);
        // return widget ok or error
        try {
            return ResponseEntity.ok()
                    .eTag( Integer.toString(widget.getVersion()))
                    .location(new URI("/rest/widget/" + widget.getId()))
                    .body( widget);
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/rest/proper/widget/{id}")
    public ResponseEntity<Widget> updateWidgetProper(@RequestBody Widget widget, @PathVariable Long id, @RequestHeader("If-Match") Integer ifMatch){
        Optional<Widget> widgetFound = widgetService.findById(id);
        if (widgetFound.isPresent()){
            if(ifMatch.equals(widgetFound.get().getVersion())){
                widget.setId(id);
                return ResponseEntity.ok().body( widgetService.save(widget));
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/rest/widget/{id}")
    public ResponseEntity deleteWidget(@PathVariable Long id){
        widgetService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
