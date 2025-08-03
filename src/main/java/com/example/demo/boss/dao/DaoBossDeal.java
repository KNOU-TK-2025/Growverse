package com.example.demo.boss.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DaoBossDeal {
	List<Map<String,Object>> SelectBossDeals(Map<String, Object> param);
	void InsertBossDeal(Map<String, Object> param);
	void DeleteBossDeal(Map<String, Object> param);
}
 