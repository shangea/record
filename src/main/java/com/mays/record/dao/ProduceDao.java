package com.mays.record.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.mays.record.mapper.ProduceMapper;
import com.mays.record.model.ProduceInfo;
import com.mays.record.util.DBUtil;

public class ProduceDao {

	private static SqlSession session;
	private static ProduceMapper mapper;

	public static void init() {
		session = DBUtil.getSqlSessionFactory().openSession();
		mapper = session.getMapper(ProduceMapper.class);
	}

	public static int insertProduceInfo(String type, int num, String worker, String time) {
		int i = mapper.insertProduceInfo(type, num, num * 25, worker, time);
		session.commit();
		return i;
	}

	public static List<ProduceInfo> selectProduceInfo(ProduceInfo info) {
		return mapper.selectProduceInfo(info);
	}

	public static int selectProduceInfoSum(ProduceInfo info) {
		Integer i = mapper.selectProduceInfoSum(info);
		return i == null ? 0 : i;
	}

	public static List<String> selectWorker() {
		return mapper.selectWorker();
	}

	public static int delProduceInfo(String id) {
		int i = mapper.delProduceInfo(id);
		session.commit();
		return i;
	}

}
