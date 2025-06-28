package com.example.demo.service;

import com.example.demo.dao.DaoPost;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;

@Service
public class ServicePost {
    @Autowired
    private SqlSessionTemplate sqlSession;
    private DaoPost daoPost = null;

    public void setup() {
        if ( this.daoPost== null) {
            this.daoPost = sqlSession.getMapper(DaoPost.class);
        }
    }

    public void get_post(Map<String, String[]> param, Map<String, Object> result) {
        this.setup();
        List<Map<String, Object>> post_list = null;
        post_list = daoPost.Select01();

        System.out.println(">>>> Select01 호출 결과: " + post_list);
        result.put("post_list", post_list);
    }

    public void new_post(Map<String, String[]> param, Map<String, Object> result) {
        this.setup();
        Map<String, Object> daoParam = new HashMap<>();

        daoParam.put("TITLE", param.get("TITLE"));

        daoPost.Insert01(daoParam);
    }

    public void delete_post(Map<String, String[]> param, Map<String, Object> result) {
        this.setup();
        Map<String, Object> daoParam = new HashMap<>();

        daoParam.put("POST_ID", param.get("POST_ID"));
        daoPost.Delete01(daoParam);
    }
}
