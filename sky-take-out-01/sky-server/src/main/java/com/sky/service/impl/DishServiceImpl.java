package com.sky.service.impl;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    /**
     * 新增菜品和对应的口味
     * 涉及多张表的操作，要保证数据的一致性，使用事务注解@Transactional
     *在启动类上开始注解方式的事务管理@EnableTransactionManagement
     * @param dishDTO
     */

    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {

        //逻辑:要新增菜品
        Dish dish = new Dish();
        //属性赋值，对象的属性拷贝，这里属性命名必须保持一致
        BeanUtils.copyProperties(dishDTO,dish);

        //向菜品表中插入1条数据,需要注入dishMapper
        dishMapper.insert(dish);//dishDTO中包含了菜品口味，这里只需要传入参数实体对象dish

        //获取insert语句生成的主键值id
        Long dishId = dish.getId();

        //口味数据 在DTO中
        List<DishFlavor> flavors = dishDTO.getFlavors();
        //口味不是必须的，需要判断一下
        //口味数据确实提交过来了
        if(flavors != null && flavors.size() >0){
            //批量插入数据之前，遍历集合
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            } );
            //向口味表中插入n条数据
            //批量插入口味集合，传入集合对象
            dishFlavorMapper.insertBatch(flavors);
        }

    }
}
