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
    public List<List<String>> searchGrid(@RequestParam("title") String title){
        log.info(title);
        List<Map<String, Object>> list = searchService.executeQueryScript("select * from search");
        List<List<String>> returnList = new ArrayList<>();
        for (int i = 0; i <list.size() ; i++) {
            List<String> stringList = new ArrayList<>();
            Map<String, Object> map = list.get(i);
            stringList.add(map.get("id").toString());
            stringList.add(map.get("title").toString());
            stringList.add(map.get("content").toString());
            stringList.add(map.get("user").toString());
            stringList.add(map.get("studyType").toString());
            returnList.add(stringList);

        }
        return returnList;
    }

    /**
     * @param draw  datatable传入参数，不作处理，返回即可
     * @param account   将要查询的账户信息，支持模糊查询
     * @param index 分页查询参数
     * @param size  分页查询参数
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/listloginfo")
    public String logInfoPaging(Integer draw,
                                @RequestParam(value = "search[value]",required = false) String search,
                                @RequestParam(value = "start",defaultValue = "1") Integer index,
                                @RequestParam(value = "length",defaultValue = "10") Integer size) {
        log.info(search+":aa");
        log.info(index+":bb");
        log.info(size+":ccc");
        final List<Map<String, Object>> list = searchService.executeQueryScript("select * from search");
        Map<String,Object> map = new HashMap<>();
        /**
         * "draw": 1,
         * 	"page": 0,
         * 	"length": 5, // 一页显示的数据条数
         * 	"recordsTotal": 44298,
         * 	"pages": 8860, // 总页数
         */
        map.put("draw",draw);
        map.put("page",index);
        map.put("length",size);
        map.put("draw",draw);
        map.put("recordsTotal","1000");
        map.put("pages","3");
        map.put("data",list);
        return JsonUtil.obj2Json(map);
    }
}
