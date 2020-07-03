package com.css.examination.services.impl;

import com.css.examination.dao.BaseDao;
import com.css.examination.services.ISearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class SearchServiceImpl implements ISearchService {
    @Autowired
    private BaseDao baseDao;

    @Override
    public List<Map<String, Object>> executeQueryScript(String sql) {
        return baseDao.executeQueryScript(sql);
    }

    @Override
    public List<Map<String, Object>> executeQuerySql(String cols, String tableName, String where, String limit) {
        return baseDao.executeQuerySql(cols,tableName,where,limit);
    }

    @Override
    public int executeInsertSql(String cols, String tableName, String values) {
        return baseDao.executeInsertSql(cols,tableName,values);
    }

    @Override
    public void executeSql(List<Map<String,String>> setList,String tableName,String where) {
        final StringBuffer sql = new StringBuffer();
        baseDao.executeSql(sql.toString());
    }
}
