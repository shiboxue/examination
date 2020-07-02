package com.css.examination.controller;

import com.css.examination.services.ISearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController
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

    @RequestMapping("/test1")
    public List<Map<String, Object>> test1(@RequestParam("msg") String msg){
        log.info(msg);
        List<Map<String, Object>> list = searchService.executeQueryScript("select * from dm_yyjk_gy_zxt");
        return list;
    }
}
