package com.css.examination.controller;

import com.css.examination.services.ISearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;


@Controller
public class SearchController {

    private Logger log = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    private ISearchService searchService;

    @RequestMapping("/test")
    public String test(@RequestParam("msg") String msg){
        log.info(msg);
        List<Map<String, Object>> list = searchService.executeQueryScript("select * from user");
        return msg;
    }

    @RequestMapping("/searchGrid")
    public List<Map<String, Object>> searchGrid(@RequestParam("title") String title){
        log.info(title);
        List<Map<String, Object>> list = searchService.executeQueryScript("select * from search");
        return list;
    }

    @RequestMapping("/testTemp")
    public String testTemp(){
        return "admin";
    }
}
