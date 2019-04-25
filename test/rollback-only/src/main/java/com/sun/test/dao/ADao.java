package com.sun.test.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ADao {

    int insert(@Param("value") String value);

}
