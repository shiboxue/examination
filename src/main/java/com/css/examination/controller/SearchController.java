package com.css.examination.controller;

import com.css.examination.services.ISearchService;
import com.css.examination.utils.DateUtils;
import com.css.examination.utils.ElasticUtils;
import com.css.examination.utils.JsonUtil;
import com.css.examination.vo.ElasticQueryVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SearchController {

    private Logger log = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    private ISearchService searchService;

    @Resource
    ElasticUtils elasticUtils;

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
            where = queryCondition(search);
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
            where = queryCondition(search);
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
        map.put("pages",list.size()==0?0:Integer.parseInt(total)/list.size());//有多少页
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
        final String cols = "title,content,user,study_type,create_time";
        final StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("(");
        for(String key:params.keySet()){
            final String value = params.get(key).toString();//
            stringBuffer.append("'");
            stringBuffer.append(value.replaceAll("'", "\\\\\'"));
            stringBuffer.append("'");
            stringBuffer.append(",");
        }
//        int i = stringBuffer.lastIndexOf(",");
//        stringBuffer.deleteCharAt(i);
        stringBuffer.append("'");
        stringBuffer.append(DateUtils.getNow());
        stringBuffer.append("'");
        stringBuffer.append(")");
        final String values = stringBuffer.toString();
        final int search = searchService.executeInsertSql(cols, "search", values);
        final List<Map<String,Object>> list = new ArrayList<>();
        list.add(params);
        try {
            elasticUtils.bulkIndex("search","user",list);
        } catch (IOException e) {
            log.error("i/o异常");
        }
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
        elasticUtils.deleteIndexDoc("search","user",params.get("id").toString());
        final Map<String,Object> returnMap = new HashMap<>();
        if (search>0){
            returnMap.put("state","1");
        }else{
            returnMap.put("state","0");
        }
        return JsonUtil.obj2Json(returnMap);
    }

    /**
     * @name DataTable 修改
     * @param params json
     * @return json串
     */
    @ResponseBody
    @RequestMapping(value = "/saveSearchGrid")
    public String saveSearchGrid(@RequestBody Map<String, Object> params) {
        final String id = params.get("id").toString();
        params.remove("id");
        params.put("content",String.valueOf(params.get("content")).replaceAll("'", "\\\\\'"));
        params.put("title",String.valueOf(params.get("title")).replaceAll("'", "\\\\\'"));
        final int search = searchService.executeUpdateSql(params, "search", id);
        elasticUtils.updateIndexDoc("search","user",id,params);
        final Map<String,Object> returnMap = new HashMap<>();
        if (search==1){
            returnMap.put("state","1");
        }else if (search==2){
            returnMap.put("state","2");
        }else {
            returnMap.put("state","0");
        }
        //elasticUtils.updateIndexDoc("search","user","");
        return JsonUtil.obj2Json(returnMap);
    }

    private String queryCondition(String search){
        String where = "";
        where = "content like '%" + search+"%'";
        where += " or ";
        where += "title like '%" + search+"%'";
        where += " or ";
        where += "user like '%" + search+"%'";
        where += " or ";
        where += "study_type like '%" + search+"%'";
        return where;
    }

    /**
     * @name 将数据库中的数据插入到ES中
     * @author shiboxue
     * @date 2020-07-21
     */
    @GetMapping(value = "/insertEs")
    public String insertEs() {
        final String where = "";
        try {
            final List<Map<String, Object>> list = searchService.executeQuerySql("*","search",where,"");
            elasticUtils.bulkIndex("search","user",list);
        } catch (IOException e) {
            log.error("i/o异常");
        }
        return "/test.html";
    }

    /**
     * @name 删除根据_id
     * @author shiboxue
     * @desc id是唯一标识，是_id
     * @date 2020-07-21
     */
    @GetMapping(value = "/deleteEsById")
    public String deleteEsById(@RequestParam String id) {
        elasticUtils.deleteIndexDoc("sbx","user",id);
        return "/test.html";
    }

    /**
     * @name 删除根据_id
     * @author shiboxue
     * @desc id是唯一标识，是_id
     * @date 2020-07-21
     */
    @GetMapping(value = "/deleteEsBySearch")
    public String deleteEsBySearch(@RequestParam String search) {
        search = "\"id\":1 ";
        elasticUtils.deleteIndexDocBySearch("search","user",search);
        return "/test.html";
    }

    /**
     * @name bootstrapTable 后端分页
     * @param search 关键字条件
     * @param pageNumber 当前页，默认是1
     * @param pageSize 显示条数，默认是10
     * @return json串
     */
    @RequestMapping("/searchGridByEs")
    @ResponseBody
    public String searchGridByEs(
            @RequestParam("search") String search,
            @RequestParam("pageNumber") Integer pageNumber,
            @RequestParam("pageSize") Integer pageSize ){
        String json = JsonUtil.obj2Json(elasticUtils.baseSearch("search", "user", search, pageNumber, pageSize,"id","ASC"));
        return  json;
    }


}
