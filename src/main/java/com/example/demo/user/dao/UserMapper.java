package com.example.demo.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.user.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * (User)表数据库访问层
 *
 * @author makejava
 * @since 2020-11-27 11:21:07
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}