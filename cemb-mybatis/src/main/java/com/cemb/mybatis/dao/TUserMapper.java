package com.cemb.mybatis.dao;

import com.cemb.mybatis.annotation.Param;
import com.cemb.mybatis.entity.Tuser;

import java.util.List;

public interface TUserMapper {

    List<Tuser> selectAll();

    Tuser selectById(String id);

    Tuser selectByNameAndId(@Param("id") String id, @Param("username") String username);
}
