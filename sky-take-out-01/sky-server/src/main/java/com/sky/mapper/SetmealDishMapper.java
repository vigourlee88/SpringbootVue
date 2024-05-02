package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /**
     * 根据菜品id查询对应的套餐id
     * 多对多的关系,多个套餐
     * @param dishIds
     * @return
     */
    //select setmeal_id from setmeal_id where dish_id IN #{1,2,3,4...} 动态拼接菜品id List集合
    List<Long> getSetmealIdsByDishIds(List<Long> dishIds);

}
