package com.example.demo.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.utils.DataTablesParams;
import com.example.demo.utils.DataTablesResult;
import org.springframework.stereotype.Service;
import com.example.demo.user.dao.UserMapper;
import com.example.demo.user.entity.User;
import com.example.demo.user.service.UserService;

import javax.annotation.Resource;

/**
 * (User)表服务实现类
 *
 * @author makejava
 * @since 2020-11-27 11:21:09
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public DataTablesResult<User> getUserTable(DataTablesParams params) {
        DataTablesResult<User> userDataTablesResult=new DataTablesResult<>();
        Page<User> page=new Page<>(params.getStart(),params.getLength());
        QueryWrapper<User> userQueryWrapper=new QueryWrapper<>();
        Page<User> userPage = userMapper.selectPage(page, userQueryWrapper);
        Integer size = userMapper.selectCount(null);
        userDataTablesResult.setData(userPage.getRecords());
        userDataTablesResult.setRecordsFiltered(size);
        userDataTablesResult.setRecordsTotal(size);
        return userDataTablesResult;
    }
}