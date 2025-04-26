package com.project.TransactionDemo.controller;

import com.project.TransactionDemo.dto.OrderRequest;
import com.project.TransactionDemo.dto.OrderResponse;
import com.project.TransactionDemo.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders/")
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest) {
        return new ResponseEntity<OrderResponse>(orderService.placeOrder(orderRequest), HttpStatus.CREATED);
    }

}
/*
curl --location 'http://localhost:8080/api/v1/orders/' \
--header 'Content-Type: application/json' \
--data '{
    "order":{
        "totalquantity":3,
        "shoppingCartId":1,
        "totalPrice":5000
    },
    "payment":{
        "type":"Credit",
        "cardName":"Snehil Gupta",
        "cardNumber":"1234 1234 1234",
        "expiryMonth":2,
        "expiryYear":2025,
        "cvc":123
    }
}'
 */