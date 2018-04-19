package com.mays.record.model;

import java.util.Date;

public class ProduceInfo {
	
	private Integer id;
	private String type;
	private Integer packNum;
	private Integer weight;
	private String worker;
	private String produceTime;
	private Date createTime;

	private String startTime;
	private String endTime;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getPackNum() {
		return packNum;
	}
	public void setPackNum(Integer packNum) {
		this.packNum = packNum;
	}
	public Integer getWeight() {
		return weight;
	}
	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	public String getWorker() {
		return worker;
	}
	public void setWorker(String worker) {
		this.worker = worker;
	}
	public String getProduceTime() {
		return produceTime;
	}
	public void setProduceTime(String produceTime) {
		this.produceTime = produceTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

}
