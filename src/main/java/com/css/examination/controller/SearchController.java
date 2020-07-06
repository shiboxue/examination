package com.css.examination.controller;

import com.css.examination.services.ISearchService;
import com.css.examination.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class SearchController {

    private Logger log = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    private ISearchService searchService;

    /**
     * @name bootstrapTable 后端分页
     * @param search 关键字条件
     * @param pageNumber 当前页，默认是1
     * @param pageSize 显示条数，默认是10
     * @return json串
     */
    @RequestMapping("/searchGrid")
    @ResponseBody
    public String searchGrid(
            @RequestParam("search") String search,
            @RequestParam("pageNumber") Integer pageNumber,
            @RequestParam("pageSize") Integer pageSize ){
        final Map<String,Object> map = new HashMap<>();
        String where = null;
        if (!StringUtils.isEmpty(search)){
            where = "content like '%" + search+"%'";
            where += " or ";
            where += "title like '%" + search+"%'";
        }
        final List<Map<String, Object>> countList = searchService.executeQuerySql("count(1) count", "search", where, null);
        final List<Map<String, Object>> list = searchService.executeQuerySql("*","search",where,(pageNumber-1)*pageSize+","+pageSize);
        map.put("total",countList.get(0).get("count"));
        map.put("rows",list);
        return JsonUtil.obj2Json(map);
    }

    /**
     * @name DataTable 后端分页
     * @param draw  datatable传入参数，不作处理，返回即可
     * @param search 关键字条件
     * @param pageNumber 当前页，默认是0
     * @param pageSize 显示条数，默认是10
     * @return json串
     */
    @ResponseBody
    @RequestMapping(value = "/listloginfo")
    public String logInfoPaging(Integer draw,
        @RequestParam(value = "search[value]",required = false) String search,
        @RequestParam(value = "start",defaultValue = "0") Integer pageNumber,
        @RequestParam(value = "length",defaultValue = "10") Integer pageSize) {
        String where = null;
        if (!StringUtils.isEmpty(search)){
            where = "content like '%" + search+"%'";
            where += " or ";
            where += "title like '%" + search+"%'";
        }
        final List<Map<String, Object>> countList = searchService.executeQuerySql("count(1) count", "search", where, null);
        final List<Map<String, Object>> list = searchService.executeQuerySql("*","search",where,pageNumber+","+pageNumber+pageSize);
        final String total = countList.get(0).get("count").toString();
        final Map<String,Object> map = new HashMap<>();
        map.put("draw",draw);
        map.put("page",pageNumber);//起
        map.put("length",pageSize);//多少条
        map.put("recordsFiltered",total);
        map.put("recordsTotal",total);
        map.put("pages",Integer.parseInt(total)/list.size());//有多少页
        map.put("data",list);//数据
        return JsonUtil.obj2Json(map);
    }

    /**
     * @name DataTable 新增
     * @param params json
     * @return json串
     */
    @ResponseBody
    @RequestMapping(value = "/addSearchGrid")
    public String addSearchGrid(@RequestBody Map<String, Object> params) {
        log.info(params.toString());
        final String cols = String.valueOf(params.get("cols"));
        final String values = String.valueOf(params.get("values"));
        final int search = searchService.executeInsertSql(cols, "search", values);
        final Map<String,Object> returnMap = new HashMap<>();
        if (search>0){
            returnMap.put("state","1");
        }else{
            returnMap.put("state","0");
        }
        return JsonUtil.obj2Json(returnMap);
    }

    /**
     * @name DataTable 删除
     * @param params id主键
     * @return json串
     */
    @ResponseBody
    @RequestMapping(value = "/deleteSearchGrid")
    public String deleteSearchGrid(@RequestBody Map<String, Object> params) {
        log.info(params.toString());
        final int search = searchService.executeDeleteSql(params.get("id").toString(), "search");
        final Map<String,Object> returnMap = new HashMap<>();
        if (search>0){
            returnMap.put("state","1");
        }else{
            returnMap.put("state","0");
        }
        return JsonUtil.obj2Json(returnMap);
    }

    /**
     * @name DataTable 新增
     * @param params json
     * @return json串
     */
    @ResponseBody
    @RequestMapping(value = "/saveSearchGrid")
    public String saveSearchGrid(@RequestBody Map<String, Object> params) {
        final String id = params.get("id").toString();
        params.remove("id");
        final int search = searchService.executeUpdateSql(params, "search", id);
        final Map<String,Object> returnMap = new HashMap<>();
        if (search==1){
            returnMap.put("state","1");
        }else if (search==2){
            returnMap.put("state","2");
        }else {
            returnMap.put("state","0");
        }
        return JsonUtil.obj2Json(returnMap);
    }


}
