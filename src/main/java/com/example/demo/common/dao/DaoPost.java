package com.example.demo.common.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DaoPost {
	List<Map<String,Object>> Select01();

	int Insert01(Map<String, Object> param);
	int Delete01(Map<String, Object> param);
}
 