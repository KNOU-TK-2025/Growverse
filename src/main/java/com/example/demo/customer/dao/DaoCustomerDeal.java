package com.example.demo.customer.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DaoCustomerDeal {
	List<Map<String,Object>> Select01(Map<String, Object> param);
	void Insert01(Map<String, Object> param);

	List<Map<String,Object>> SelectPopularCourses(Map<String, Object> param);
	List<Map<String,Object>> SelectNewCourse(Map<String, Object> param);
	void InsertNewCourse(Map<String, Object> param);
	void InsertDealOnNewCourse(Map<String, Object> param);
}
 