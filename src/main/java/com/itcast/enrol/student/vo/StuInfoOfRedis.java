package com.itcast.enrol.student.vo;

import java.io.Serializable;

public class StuInfoOfRedis implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String mobile;
	private String name;
	
	public StuInfoOfRedis(){
		
	}
	
	public StuInfoOfRedis(Long id,String mobile,String name){
		this.id=id;
		this.mobile=mobile;
		this.name=name;
	}
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
