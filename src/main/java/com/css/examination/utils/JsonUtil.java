package com.css.examination.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;


/**
 *
 * @project 大数据云平台局项目
 * @package com.css.common.util
 * @file JsonUtil.java 创建时间:2016年11月14日上午10:35:31
 * @title 标题（要求能简洁地表达出类的功能和职责）
 * @description 描述（简要描述类的职责、实现方式、使用注意事项等）
 * @copyright Copyright (c) 2016 中国软件与技术服务股份有限公司
 * @company 中国软件与技术服务股份有限公司
 * @module 模块: 模块名称
 * @author   商健
 * @reviewer 审核人
 * @version 1.0
 * @history 修订历史（历次修订内容、修订人、修订时间等）
 *
 */
public class JsonUtil {


    /**
     * @description 字段功能描述
     * @value value:mapper
     */
     private static ObjectMapper mapper = new ObjectMapper();

    /**
     *@name    json转map
     *@description 相关说明
     *@time    创建时间:2016年11月14日上午10:35:45
     *@param jsonStr jsonStr
     *@return map
     *@author   商健
     *@history 修订历史（历次修订内容、修订人、修订时间等）
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> str2Map(String jsonStr) {
        Map<String, Object> map = null;
        try {
            map = mapper.readValue(jsonStr, Map.class);
        } catch (Exception e) {
        }
        return map;
    }

    /**
     *@name    json 转list
     *@description 相关说明
     *@time    创建时间:2016年11月14日上午10:35:00
     *@param jsonStr jsonStr
     *@return list
     *@author   商健
     *@history 修订历史（历次修订内容、修订人、修订时间等）
     */
    @SuppressWarnings("rawtypes")
    public static List str2List(String jsonStr) {
        List list = null;
        try {
            list = mapper.readValue(jsonStr, List.class);
        } catch (Exception e) {
        }
        return list;
    }

    /**
     *@name   对象转json
     *@description 相关说明
     *@time    创建时间:2016年11月14日上午10:35:56
     *@param obj obj
     *@return json
     *@author   商健
     *@history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static String obj2Json(Object obj) {
        String jsonStr = null;
        try {
           jsonStr = mapper.writeValueAsString(obj);
        } catch (Exception e) {
        }
        return jsonStr;
    }
    /**
     *@name   对象转json
     *@description 相关说明
     *@time    创建时间:2016年11月14日上午10:35:56
     *@param   data
     *@param   t
     *@return json
     *@author   liwei
     *@history 修订历史（历次修订内容、修订人、修订时间等）
     */
    public static <T> T jsonToVO(String data,Class<T> t) throws IllegalAccessException, InstantiationException {
        return t.newInstance();
    }
}
