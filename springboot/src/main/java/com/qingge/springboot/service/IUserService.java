package com.qingge.springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qingge.springboot.controller.dto.UserDTO;
import com.qingge.springboot.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 青哥哥
 * @since 2024-07-15
 */
public interface IUserService extends IService<User> {
    Boolean login(UserDTO userDTO);
}
