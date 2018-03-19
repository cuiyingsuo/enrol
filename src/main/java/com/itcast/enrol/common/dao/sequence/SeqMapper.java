package com.itcast.enrol.common.dao.sequence;


/**
 * 
 * SeqMapper数据库操作接口类
 * 
 **/

public interface SeqMapper{

	public String nextVal(String seqName);

}