package com.css.examination.services.impl;

import com.css.examination.dao.BaseDao;
import com.css.examination.services.ISearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

/**
 * @className SearchServiceImpl
 * @Description TODO
 * @Auhtor shiboxue
 * @Date 2019/9/12 9:29
 * @Version 1.0
 **/
@Service
public class SearchServiceImpl implements ISearchService {
    @Autowired
    private BaseDao baseDao;

    @Override
    public List<Map<String, Object>> executeQueryScript(String sql) {
        return baseDao.executeQueryScript(sql);
    }
}
