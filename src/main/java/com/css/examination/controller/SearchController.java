package com.css.examination.controller;

import com.css.examination.services.ISearchService;
import com.css.examination.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
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
    @ResponseBody
    public String searchGrid(@RequestParam("search") String search){
        List<Map<String, Object>> list = searchService.executeQueryScript("select * from search");
        return JsonUtil.obj2Json(list);
    }

    /**
     * @name DataTable 后端分页
     * @param draw  datatable传入参数，不作处理，返回即可
     * @param search 关键字条件
     * @param page 当前页，默认是0
     * @param size 显示条数，默认是10
     * @return json串
     */
    @ResponseBody
    @RequestMapping(value = "/listloginfo")
    public String logInfoPaging(Integer draw,
        @RequestParam(value = "search[value]",required = false) String search,
        @RequestParam(value = "start",defaultValue = "0") Integer page,
        @RequestParam(value = "length",defaultValue = "10") Integer size) {
        final List<Map<String, Object>> list = searchService.executeQueryScript("select * from search");
        Map<String,Object> map = new HashMap<>();
        /**

         * 	"recordsTotal": 44298,
         * 	"pages": 8860, // 总页数
         */
        map.put("draw",draw);
        map.put("page",page);
        map.put("length",size);
        map.put("draw",draw);
        map.put("recordsTotal","1000");
        map.put("pages","3");
        map.put("data",list);
        return JsonUtil.obj2Json(map);
    }
}
