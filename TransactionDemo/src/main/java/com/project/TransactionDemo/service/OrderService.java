package com.project.TransactionDemo.service;

import com.project.TransactionDemo.dto.OrderRequest;
import com.project.TransactionDemo.dto.OrderResponse;

public interface OrderService {

    OrderResponse placeOrder(OrderRequest orderRequest);

}
