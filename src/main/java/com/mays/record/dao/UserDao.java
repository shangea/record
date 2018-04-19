package com.mays.record.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.mays.record.mapper.UserMapper;
import com.mays.record.model.UserInfo;
import com.mays.record.util.DBUtil;

public class UserDao {

	private static SqlSession session;
	private static UserMapper mapper;

	public static void init() {
		session = DBUtil.getSqlSessionFactory().openSession();
		mapper = session.getMapper(UserMapper.class);
	}

	public static UserInfo selectUserInfoById(int id) {
		return mapper.selectUserInfoById(id);
	}

	public static List<String> selectUserByType(int type) {
		return mapper.selectUserByType(type);
	}

	public static List<String> selectAllUserName() {
		return mapper.selectAllUserName();
	}

	public static int insertUserInfo(String userName, int type) {
		int i = mapper.insertUserInfo(userName, type);
		session.commit();
		return i;
	}

	public static int delUserInfo(String userName) {
		int i = mapper.delUserInfo(userName);
		session.commit();
		return i;
	}

}
