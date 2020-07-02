package com.css.examination.utils;


import com.css.examination.vo.ElasticQueryVO;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/***
 * ElasticSearch 工具类
 */
@Service
public class ElasticUtils {
    @Qualifier("LocalRestHighLevelClient")
    @Autowired
    RestHighLevelClient client;
    private static Logger logger = LoggerFactory.getLogger(ElasticUtils.class);

    /**
     * 根据sourcebuilder 来查询
     *
     * @param queryVO 索引名称  ，字段名  字段值
     * @return 查询结果
     * @throws IOException
     */
    public List<Map<String, Object>> queryBySearchSourceBulder(ElasticQueryVO queryVO) throws IOException {
        final List<Map<String, Object>> queryResult = new ArrayList<Map<String, Object>>();
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(queryVO.getIndexName());
        /*****高亮显示*****/
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        HighlightBuilder.Field highlightTitle =
                new HighlightBuilder.Field(queryVO.getIndexName());
        highlightTitle.highlighterType("title");
        highlightBuilder.preTags("<span style=\"color:red\">");   //高亮设置
        highlightBuilder.postTags("</span>");
        highlightBuilder.field(highlightTitle);
        HighlightBuilder.Field highlightUser = new HighlightBuilder.Field(queryVO.getQueryColunm());
        highlightBuilder.field(highlightUser);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.matchPhraseQuery(queryVO.getQueryColunm(), queryVO.getQueryValue()));
        sourceBuilder.from(0);
        sourceBuilder.size(10000);
        sourceBuilder.highlighter(highlightBuilder);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        for (SearchHit hit : hits.getHits()) {
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField highlight = highlightFields.get(queryVO.getQueryColunm());
            Text[] fragments = highlight.fragments();
            String fragmentString = fragments[0].string();
            final Map<String, Object> currentMap = hit.getSourceAsMap();
            currentMap.put("content", fragmentString);
            queryResult.add(currentMap);
        }
        return queryResult;
    }
    /**
     * 根据sourcebuilder 来查询
     *
     * @param queryVO 索引名称  ，字段名  字段值
     * @return 查询结果
     * @throws IOException
     */
    public List<Map<String, Object>> queryBySmartSearchSourceBulder(ElasticQueryVO queryVO) throws IOException {
        final List<Map<String, Object>> queryResult = new ArrayList<Map<String, Object>>();
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(queryVO.getIndexName());
        /*****高亮显示*****/
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        HighlightBuilder.Field highlightTitle =
                new HighlightBuilder.Field(queryVO.getIndexName());
        highlightTitle.highlighterType("title");
        highlightBuilder.preTags("<span style=\"color:red\">");   //高亮设置
        highlightBuilder.postTags("</span>");
        highlightBuilder.field(highlightTitle);
        HighlightBuilder.Field highlightUser = new HighlightBuilder.Field(queryVO.getQueryColunm());
        highlightBuilder.field(highlightUser);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.matchQuery(queryVO.getQueryColunm(), queryVO.getQueryValue()));
        sourceBuilder.from(0);
        sourceBuilder.size(10000);
        sourceBuilder.highlighter(highlightBuilder);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        for (SearchHit hit : hits.getHits()) {
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField highlight = highlightFields.get(queryVO.getQueryColunm());
            Text[] fragments = highlight.fragments();
            String fragmentString = fragments[0].string();
            final Map<String, Object> currentMap = hit.getSourceAsMap();
            currentMap.put("content", fragmentString);
            queryResult.add(currentMap);
        }
        return queryResult;
    }
}