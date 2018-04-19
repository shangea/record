package com.mays.record.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.mays.record.mapper.SalesMapper;
import com.mays.record.model.SalesInfo;
import com.mays.record.util.DBUtil;

public class SalesDao {

	private static SqlSession session;
	private static SalesMapper mapper;

	public static void init() {
		session = DBUtil.getSqlSessionFactory().openSession();
		mapper = session.getMapper(SalesMapper.class);
	}

	public static int insertSalesInfo(String type, int weight, String buyer, String time) {
		int i = mapper.insertSalesInfo(type, weight, buyer, time);
		session.commit();
		return i;
	}

	public static List<SalesInfo> selectSalesInfo(SalesInfo info) {
		return mapper.selectSalesInfo(info);
	}

	public static int selectSalesInfoSum(SalesInfo info) {
		Integer i = mapper.selectSalesInfoSum(info);
		return i == null ? 0 : i;
	}

	public static List<String> selectBuyer() {
		return mapper.selectBuyer();
	}

	public static int delSalesInfo(String id) {
		int i = mapper.delSalesInfo(id);
		session.commit();
		return i;
	}

}
