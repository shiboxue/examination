package com.css.examination.services;

import java.util.List;
import java.util.Map;

/**
 *
 */
public interface ISearchService {

    List<Map<String,Object>> executeQueryScript(String sql);
}
