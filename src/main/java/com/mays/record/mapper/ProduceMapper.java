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

import com.mays.record.model.ProduceInfo;
import com.mays.record.util.StringUtils;

public interface ProduceMapper {

	/**
	 * 添加生产记录
	 * @author	mays
	 * @date	2018年4月18日
	 */
	@Insert("insert into produce_info (type,pack_num,weight,worker,produce_time) values "
			+ "(#{type},#{num},#{weight},#{worker},#{time})")
	int insertProduceInfo(@Param("type") String type, @Param("num") int num, @Param("weight") int weight,
			@Param("worker") String worker, @Param("time") String time);

	/**
	 * 生产记录查询
	 * @author	mays
	 * @date	2018年4月18日
	 */
	@SelectProvider(type = SqlProvider.class, method = "selectProduceInfo")
	@ResultType(ProduceInfo.class)
	List<ProduceInfo> selectProduceInfo(@Param("info") ProduceInfo info);

	/**
	 * 生产记录查询统计
	 * @author	mays
	 * @date	2018年4月18日
	 */
	@SelectProvider(type = SqlProvider.class, method = "selectProduceInfoSum")
	Integer selectProduceInfoSum(@Param("info") ProduceInfo info);

	/**
	 * 查询生产表里的员工
	 * @author	mays
	 * @date	2018年4月18日
	 */
	@Select("select worker from produce_info group by worker")
	List<String> selectWorker();

	/**
	 * 删除记录
	 * @author	mays
	 * @date	2018年4月18日
	 */
	@Delete("delete from produce_info where id = #{id}")
	int delProduceInfo(@Param("id") String id);

	public class SqlProvider {

		public String selectProduceInfo(Map<String, Object> param) {
			final ProduceInfo info = (ProduceInfo) param.get("info");
			SQL sql = new SQL() {
				{
					SELECT("*");
					FROM("produce_info");
					ORDER_BY("produce_time");
				}
			};
			where(sql, info);
			return sql.toString();
		}

		public String selectProduceInfoSum(Map<String, Object> param) {
			final ProduceInfo info = (ProduceInfo) param.get("info");
			SQL sql = new SQL() {
				{
					SELECT("SUM(pack_num)");
					FROM("produce_info");
				}
			};
			where(sql, info);
			return sql.toString();
		}

		private void where(SQL sql, final ProduceInfo info) {
			if (!"全部".equals(info.getWorker())) {
				sql.WHERE("worker = #{info.worker}");
			}
			if (!"全部".equals(info.getType())) {
				sql.WHERE("type = #{info.type}");
			}
			if (StringUtils.isNotEmpty(info.getStartTime())) {
				sql.WHERE("produce_time >= #{info.startTime}");
			}
			if (StringUtils.isNotEmpty(info.getEndTime())) {
				sql.WHERE("produce_time <= #{info.endTime}");
			}
		}

	}

}
