package com.itcast.enrol.common.base;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * Created by liuzhongshuai on 16/8/14.
 */
public interface BaseDao<T> extends Mapper<T>, MySqlMapper<T> {



}
