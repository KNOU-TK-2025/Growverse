package com.example.demo.boss.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DaoBoss {
	List<Map<String,Object>> SelectBoss(Map<String, Object> param);
	void UpdateBoss(Map<String, Object> param);
}
 