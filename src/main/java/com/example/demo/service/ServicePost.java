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

        result.put("post_list", post_list);
    }

    public void new_post(Map<String, String[]> param, Map<String, Object> result) {
        this.setup();
        Map<String, Object> daoParam = new HashMap<>();

        System.out.println("123213" + param.toString() + "<<<<<");
        System.out.println("123213" + param.keySet() + "<<<<<");
        System.out.println("123213" + param.get("TITLE") + "<<<<<");
        daoParam.put("TITLE", param.get("TITLE"));
        System.out.println(">>>>>>" + daoParam.toString() + "<<<<<");

        daoPost.Insert01(daoParam);
    }

    public void delete_post(Map<String, String[]> param, Map<String, Object> result) {
        this.setup();
        Map<String, Object> daoParam = new HashMap<>();

        daoParam.put("POST_ID", param.get("POST_ID"));
        daoPost.Delete01(daoParam);
    }
}
