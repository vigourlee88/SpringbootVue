package com.qingge.springboot.service.impl;

import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qingge.springboot.controller.dto.UserDTO;
import com.qingge.springboot.entity.User;
import com.qingge.springboot.mapper.UserMapper;
import com.qingge.springboot.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 青哥哥
 * @since 2024-07-15
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private static final Log LOG= Log.get();

    
    @Override
    public Boolean login(UserDTO userDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();//构建QueryWrapper对象
        queryWrapper.eq("username",userDTO.getUsername());
        queryWrapper.eq("password",userDTO.getPassword());


//        List<User> list = list(queryWrapper);
//        return list.size() != 0;
        try{
            User one = getOne(queryWrapper);
            return one != null;
        }catch(Exception e){
            LOG.error(e);
            return false;

        }
    }
}
