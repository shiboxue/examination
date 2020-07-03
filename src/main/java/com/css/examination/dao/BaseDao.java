package com.css.examination.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by wangshihui on 2019/3/29.
 * sql脚本基本操作
 */
@Component
@Mapper
public interface BaseDao {
    /**
     * 执行sql脚本更新操作，多个用;分隔
     *
     * @param sql
     */
    void executeSql(String sql);

    /**
     * 执行sql脚本查询操作
     * * @param cols  tableName
     *
     * @param tableName 表名称
     * @param where     查询条件
     * @param limit     限制条数
     * @return listmap
     */
    List<Map<String, Object>> executeQuerySql(@Param("cols") String cols, @Param("tableName") String tableName, @Param("where") String where, @Param("limit") String limit);

    /**
     * 执行sql脚本查询操作
     * * @param sql  sql
     *
     * @return listmap
     */
    List<Map<String, Object>> executeQueryScript(String sql);

    /**
     * 执行sql脚本更新操作
     *
     * @param tableName 表名称
     * @param cols      列字符串，用逗号分隔
     * @param values    值得字符串，用逗号分隔
     * @return 插入条数
     */
    int executeInsertSql(@Param("cols") String cols, @Param("tableName") String tableName, @Param("values") String values);

    /**
     * 执行sql脚本删除操作
     * @param tableName 表名
     * @param id 主键
     */
    int deleteByExample(@Param("tableName") String tableName,@Param("id") String id);

    /**
     * 执行sql脚本更新操作
     * @param tableName 表名
     * @param set set
     * @param id 主键
     */
    int updateByExample(@Param("tableName") String tableName,@Param("set") String set,@Param("id") String id);

}
