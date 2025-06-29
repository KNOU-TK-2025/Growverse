package com.example.demo.customer.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface DaoCustomerDeal {
	void Insert01(Map<String, Object> param);
}
 