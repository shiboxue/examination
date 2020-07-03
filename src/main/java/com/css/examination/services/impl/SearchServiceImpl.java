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
    public int executeDeleteSql(String id, String tableName) {
        return baseDao.deleteByExample(tableName,id);
    }

    @Override
    public int executeUpdateSql(Map<String,Object> map,String tableName,String id) {
        final StringBuffer sql = new StringBuffer();
        for(String key:map.keySet()){//keySet获取map集合key的集合  然后在遍历key即可
            final String value = map.get(key).toString();//
            sql.append(",");
            sql.append(key);
            sql.append(" = '");
            sql.append(value);
            sql.append("'");
        }
        final String set = sql.substring(1);
        return baseDao.updateByExample(tableName,set,id);
    }
}
