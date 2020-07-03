package com.css.examination.services;

import java.util.List;
import java.util.Map;

/**
 * @author shiboxue
 * @Time 2020-07-03
 */
public interface ISearchService {

    /**
     * @name 执行查询脚本
     * @des 使用于sql原声查询
     * @param sql
     * @auth shiboxue
     * @return list
     */
    List<Map<String,Object>> executeQueryScript(String sql);

    /**
     * @name 执行sql脚本查询操作
     * @param tableName 表名称
     * @param where 查询条件
     * @param limit 限制条数
     * @auth shiboxue
     * @return listmap
     */
    List<Map<String, Object>> executeQuerySql(String cols, String tableName,String where,String limit);

    /**
     * @name 执行sql脚本更新操作
     * @param tableName 表名称
     * @param cols 列字符串，用逗号分隔
     * @param values 值得字符串，用逗号分隔
     * @auth shiboxue
     * @return 插入条数
     */
    int executeInsertSql(String cols,String tableName,String values);

    /**
     * @name 执行sql脚本删除操作
     * @param tableName 表名称
     * @param id 主键
     * @auth shiboxue
     * @return 删除条数
     */
    int executeDeleteSql(String id,String tableName);

    /**
     * @name 执行sql脚本更新操作，多个用;分隔
     * @param map 需要更新的值
     * @param tableName 表名
     * @param id where条件
     * @auth shiboxue
     */
    int executeUpdateSql(Map<String,Object> map,String tableName,String id);
}
