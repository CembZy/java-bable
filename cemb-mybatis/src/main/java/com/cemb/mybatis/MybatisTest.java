package com.cemb.mybatis;

import com.cemb.mybatis.dao.TUserMapper;
import com.cemb.mybatis.entity.Tuser;
import com.cemb.mybatis.session.SqlSession;
import com.cemb.mybatis.session.SqlSessionFactory;

import java.util.List;

public class MybatisTest {

    public static void main(String[] args) {

        SqlSessionFactory sqlSessionFactory = new SqlSessionFactory();

        SqlSession sqlSession = sqlSessionFactory.openSession();

        TUserMapper mapper = sqlSession.getMapper(TUserMapper.class);

        List<Tuser> tusers = mapper.selectAll();

        tusers.forEach(it -> System.out.println(it));

        Tuser tuser = mapper.selectByNameAndId("1","lison");

        System.out.println(tuser);
    }
}
