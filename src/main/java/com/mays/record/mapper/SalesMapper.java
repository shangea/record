package com.mays.record.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import com.mays.record.model.SalesInfo;
import com.mays.record.util.StringUtils;

public interface SalesMapper {

	/**
	 * 添加销售记录
	 * @author	mays
	 * @date	2018年4月19日
	 */
	@Insert("insert into sales_info (type,weight,buyer,sales_time) values "
			+ "(#{type},#{weight},#{buyer},#{time})")
	int insertSalesInfo(@Param("type") String type, @Param("weight") int weight,
			@Param("buyer") String buyer, @Param("time") String time);

	/**
	 * 销售记录查询
	 * @author	mays
	 * @date	2018年4月19日
	 */
	@SelectProvider(type = SqlProvider.class, method = "selectSalesInfo")
	@ResultType(SalesInfo.class)
	List<SalesInfo> selectSalesInfo(@Param("info") SalesInfo info);

	/**
	 * 销售记录查询统计
	 * @author	mays
	 * @date	2018年4月19日
	 */
	@SelectProvider(type = SqlProvider.class, method = "selectSalesInfoSum")
	Integer selectSalesInfoSum(@Param("info") SalesInfo info);

	/**
	 * 查询销售表里的客户
	 * @author	mays
	 * @date	2018年4月19日
	 */
	@Select("select buyer from sales_info group by buyer")
	List<String> selectBuyer();

	/**
	 * 删除记录
	 * @author	mays
	 * @date	2018年4月19日
	 */
	@Delete("delete from sales_info where id = #{id}")
	int delSalesInfo(@Param("id") String id);

	public class SqlProvider {

		public String selectSalesInfo(Map<String, Object> param) {
			final SalesInfo info = (SalesInfo) param.get("info");
			SQL sql = new SQL() {
				{
					SELECT("*");
					FROM("sales_info");
					ORDER_BY("sales_time");
				}
			};
			where(sql, info);
			return sql.toString();
		}

		public String selectSalesInfoSum(Map<String, Object> param) {
			final SalesInfo info = (SalesInfo) param.get("info");
			SQL sql = new SQL() {
				{
					SELECT("SUM(weight)");
					FROM("sales_info");
				}
			};
			where(sql, info);
			return sql.toString();
		}

		private void where(SQL sql, final SalesInfo info) {
			if (!"全部".equals(info.getBuyer())) {
				sql.WHERE("buyer = #{info.buyer}");
			}
			if (!"全部".equals(info.getType())) {
				sql.WHERE("type = #{info.type}");
			}
			if (StringUtils.isNotEmpty(info.getStartTime())) {
				sql.WHERE("sales_time >= #{info.startTime}");
			}
			if (StringUtils.isNotEmpty(info.getEndTime())) {
				sql.WHERE("sales_time <= #{info.endTime}");
			}
		}

	}

}
