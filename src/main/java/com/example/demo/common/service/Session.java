package com.example.demo.common.service;

import com.example.demo.common.dao.DaoSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class Session {
    @Autowired
    private SqlSessionTemplate sqlSession;
    private DaoSession daoSession = null;

    public void setup() {
        if ( this.daoSession == null) {
            this.daoSession = sqlSession.getMapper(DaoSession.class);
        }
    }

    public Map<String, Object> get_user_data(String sessionID) {
        this.setup();
        Map<String, Object> param = new HashMap<>();
        param.put("SESSION_ID", sessionID);
        return daoSession.Select01(param);
    }
}
