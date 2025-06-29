package com.example.demo.customer.service;

import com.example.demo.customer.dao.DaoCustomerDeal;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CustomerDeal {
    @Autowired
    private SqlSessionTemplate sqlSession;
    private DaoCustomerDeal daoCustomerDeal = null;

    public void setup() {
        if ( this.daoCustomerDeal == null) {
            this.daoCustomerDeal = sqlSession.getMapper(DaoCustomerDeal.class);
        }
    }

    public void put_customer_deal(Map<String, Object> param) {
        this.setup();
        daoCustomerDeal.Insert01(param);
    }
}
