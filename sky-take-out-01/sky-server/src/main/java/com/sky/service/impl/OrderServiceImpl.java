package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private AddressBookMapper addressBookMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;


    /**
     * 用户下单
     * @param ordersSubmitDTO
     * @return
     */
    @Transactional
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {
        //要涉及到操作order,order_detail表，就要注入相应的mapper
        //1.一般下单之前，处理各种业务异常(地址簿为空，购物车数据为空)，要操作2张表，就要注入mapper
        AddressBook addressBook = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if(addressBook == null ){
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        //获取当前用户id
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userId);//当前用户id的购物车
        List<ShoppingCart> shoppingCartList= shoppingCartMapper.list(shoppingCart);//要求传入当前用户的ShoppingCart对象

        if(shoppingCartList == null || shoppingCartList.size() == 0 ){
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }
        //2.向订单表插入1条数据
        Orders orders = new Orders();//构建订单对象
        BeanUtils.copyProperties(ordersSubmitDTO,orders);//对象属性拷贝

        orders.setOrderTime(LocalDateTime.now());
        orders.setPayStatus(Orders.UN_PAID);
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setNumber(String.valueOf(System.currentTimeMillis()));//订单号
        orders.setPhone(addressBook.getPhone());
        orders.setConsignee(addressBook.getConsignee());
        orders.setUserId(userId);

        orderMapper.insert(orders);//插入完数据之后，需要返回主键值,下面订单明细要使用，在mapper.xml中加入useGeneratedKeys="true" keyProperty="id"，id对应订单实体类的id属性

        List<OrderDetail> orderDetailList = new ArrayList<>();
        //3.向订单明细表插入n条数据
        //订单明细如何封装，遍历购物车
        for (ShoppingCart cart : shoppingCartList) {
            //每次遍历完就封装成 订单明细对象
            OrderDetail orderDetail = new OrderDetail();//订单明细
            BeanUtils.copyProperties(cart,orderDetail);
            orderDetail.setOrderId(orders.getId());//设置当前订单明细关联的id
            orderDetailList.add(orderDetail);

        }
        orderDetailMapper.insertBatch(orderDetailList);//订单明细表 批量插入list集合

        //4.用户下单成功后，清空当前用户的购物车数据
        shoppingCartMapper.deleteByUserId(userId);

        //5.封装VO对象返回结果
        OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder()
                .id(orders.getId())
                .orderTime(orders.getOrderTime())
                .orderNumber(orders.getNumber())
                .orderAmount(orders.getAmount())
                .build();//构建VO对象

        return orderSubmitVO;
    }
}
