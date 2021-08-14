package me.hjeong.orderservice.service;

import me.hjeong.orderservice.dto.OrderDto;
import me.hjeong.orderservice.jpa.OrderEntity;

public interface OrderService {

    OrderDto createOrder(OrderDto orderDto);
    OrderDto getOrderByOrderId(String orderId);
    Iterable<OrderEntity> getOrdersByUserId(String userId);

}
