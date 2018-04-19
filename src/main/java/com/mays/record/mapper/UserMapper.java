package com.mays.record.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import com.mays.record.model.UserInfo;

public interface UserMapper {

	@Select("select * from user_info where id = #{id}")
	@ResultType(UserInfo.class)
	UserInfo selectUserInfoById(int id);

	@Select("select user_name from user_info where type = #{type}")
	List<String> selectUserByType(int type);

	@Select("select user_name from user_info order by type")
	List<String> selectAllUserName();

	@Insert("insert into user_info (user_name,type) values (#{userName},#{type})")
	int insertUserInfo(@Param("userName") String userName, @Param("type") int type);

	@Delete("delete from user_info where user_name = #{userName}")
	int delUserInfo(@Param("userName") String userName);

}
