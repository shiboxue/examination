package com.css.examination.api;


import com.css.examination.utils.ElasticUtils;
import com.css.examination.utils.JsonUtil;
import com.css.examination.vo.ElasticQueryVO;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller("ExamDataAPI")
public class ExamDataAPI {
    private Logger logger = LoggerFactory.getLogger(ExamDataAPI.class);
    @Resource
    ElasticUtils elasticUtils;

    /**
     * @param title 标题
     * @return 成功删除条数
     * @name 根据表名获取表字段信息
     * @author liwei
     * @date 2020-04-30
     */
    @RequestMapping(value = "/es/exam", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> queryExaminationByName(@RequestParam String title, @RequestParam String smart) {
        if (!StringUtils.isEmpty(title)) {
            final ElasticQueryVO queryVO = new ElasticQueryVO();
            //设置索引
            queryVO.setIndexName("examinationl");
            //设置要检索的字段
            queryVO.setQueryColunm("content");
            //检索内容
            queryVO.setQueryValue(title);
            try {
                if ("true".equals(smart)) {
                    return elasticUtils.queryBySmartSearchSourceBulder(queryVO);
                } else {
                    return elasticUtils.queryBySearchSourceBulder(queryVO);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * @param title 标题
     * @return 成功删除条数
     * @name 根据表名获取表字段信息
     * @author liwei
     * @date 2020-04-30
     */
    @RequestMapping(value = "/es/exam/questions", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> queryQuestions(@RequestParam String id, @RequestParam String authorization) throws IOException {
        String reqUrl = "https://css.zhixueyun.com/api/v1/exam/exam/front/exam-paper?clientType=1&examId=" + id + "&_=1590569645536";
        //List<Map<String, Object>> mapList = handlerBaseDao.executeQueryScript("select * from answer");
        String respJson = requestWeb(reqUrl, authorization);
        Map<String, Object> resMap = JsonUtil.str2Map(respJson);
        Map<String, Object> questionMap = (Map<String, Object>) resMap.get("paper");
        List<Map<String, Object>> questionList = (List<Map<String, Object>>) questionMap.get("questions");
        return questionList;
    }

    /**
     * requestWeb for http
     */
    private String requestWeb(String REQUEST_URL, String authorization) throws IOException {
        try {
            Connection conn = getConnection(REQUEST_URL, authorization);
            Connection.Response response = conn.timeout(5000).ignoreContentType(true).execute();
            return response.body();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private Connection getConnection(String url, String authorization) {
        Connection con = Jsoup.connect(url);
        con.header("Accept", "*/*")
                .header("Authorization", authorization)
                .header("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Cookie", "orgId=; configId=")
                .header("Connection", "keep-alive")
                .header("Referer", "https://css.zhixueyun.com/")
                .header("Host", "css.zhixueyun.com")
                .header("Sec-Fetch-Dest", "empty")
                .header("Sec-Fetch-Mode", "cors")
                .header("Sec-Fetch-Site", "same-origin")
                .header("X-Requested-With", "XMLHttpRequest")
                .header("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Mobile Safari/537.36");
        return con;
    }

    /**
     * @name 查询页面
     * @author liwei
     * @date 2020-04-30
     */
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String toHome(@RequestParam String title, @RequestParam String smart) {
        return "/search/search.html";
    }
}
