package com.bext.spring5mvcwidget.controller;

import com.bext.spring5mvcwidget.model.Widget;
import com.bext.spring5mvcwidget.service.IWidgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class WidgetController {
    @Autowired
    private IWidgetService iWidgetService;

    /* load new widget page*/
    @GetMapping("/widget/new")
    public String newWidget(Model model){
        model.addAttribute("widget", new Widget());
        return "widgetform";
    }

    /* create a new widget */
    @PostMapping("/widget")
    public String createWidget(Widget widget, Model model){
        iWidgetService.save(widget);
        return "redirect:/widget/" + widget.getId();
    }

    /* get a widget by id */
    @GetMapping("/widget/{id}")
    public String getWidgetById(@PathVariable Long id, Model model){
        model.addAttribute("widget", iWidgetService.findById(id).orElse(new Widget()));
        return "widget";
    }

    /* get all widgets */
    @GetMapping("/widgets")
    public String getWidgets(Model model){
        model.addAttribute("widgets", iWidgetService.findAll());
        return "widgets";
    }

    /* load the edit widget page for widget with id */
    @GetMapping("/widget/edit/{id}")
    public String editWidget(@PathVariable Long id, Model model){
        model.addAttribute("widget", iWidgetService.findById(id).orElse(new Widget()));
        return "widgetform";
    }

    /* update a widget by id */
    @PostMapping("/widget/{id}")
    public String updateWidget(Widget widget){
        iWidgetService.save(widget);
        return "redirect:/widget/";
    }

    /* delete a widget by id */
    @GetMapping("/widget/delete/{id}")
    public String deleteWidget(@PathVariable Long id) {
        iWidgetService.deleteById(id);
        return "redirect:/widgets/";
    }
}
