package com.example.demo.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.user.entity.User;
import com.example.demo.utils.DataTablesParams;
import com.example.demo.utils.DataTablesResult;

/**
 * (User)表服务接口
 *
 * @author makejava
 * @since 2020-11-27 11:21:08
 */
public interface UserService extends IService<User> {

    DataTablesResult<User> getUserTable(DataTablesParams params);

}