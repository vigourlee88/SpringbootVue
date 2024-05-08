package com.itheima.controller;

import com.itheima.entity.User;
import com.itheima.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @PostMapping
    @CachePut(cacheNames = "userCache",key="#user.id")
//    @CachePut(cacheNames = "userCache",key="#result.id")
//    @CachePut(cacheNames = "userCache",key="#p0.id")
//    @CachePut(cacheNames = "userCache",key="#a0.id")
//    @CachePut(cacheNames = "userCache",key="#root.args[0].id")
    //如果使用spring cache 缓存数据，key的生成: userCache::spring EL result代表返回值user对象
    public User save(@RequestBody User user){
        userMapper.insert(user);
        return user;
    }

    @DeleteMapping
    @CacheEvict(cacheNames = "userCache",key="#id")
    //将一条或多条数据从缓存中删除
    public void deleteById(Long id){
        userMapper.deleteById(id);
    }

	@DeleteMapping("/delAll")
    @CacheEvict(cacheNames = "userCache",allEntries = true)
    //删除所有userCache下键值对
    public void deleteAll(){
        userMapper.deleteAll();
    }

    @GetMapping
    @Cacheable(cacheNames = "userCache",key="#id")
    //在方法执行前先查询缓存中是否有数据，有直接返回。没有调用方法，并将方法返回值放到缓存中
    public User getById(Long id){
        User user = userMapper.getById(id);
        return user;
    }

}
