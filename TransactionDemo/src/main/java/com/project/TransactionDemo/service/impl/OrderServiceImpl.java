package com.project.TransactionDemo.service.impl;

import com.project.TransactionDemo.dto.OrderRequest;
import com.project.TransactionDemo.dto.OrderResponse;
import com.project.TransactionDemo.entity.Order;
import com.project.TransactionDemo.entity.Payment;
import com.project.TransactionDemo.exception.PaymentException;
import com.project.TransactionDemo.repository.OrderRepository;
import com.project.TransactionDemo.repository.PaymentRepository;
import com.project.TransactionDemo.service.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    OrderRepository orderRepository;
    PaymentRepository paymentRepository;

    public OrderServiceImpl(OrderRepository orderRepository, PaymentRepository paymentRepository) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    @Transactional
    public OrderResponse placeOrder(OrderRequest orderRequest) {

        Order order=orderRequest.getOrder();
        order.setStatus("INPROGRESS");
        order.setOrderTrackingNumber(UUID.randomUUID().toString());
        orderRepository.save(order);

        Payment payment=orderRequest.getPayment();

        if(payment.getType().equals("DEBIT")){
            throw new PaymentException("Payment card type do not support");
        }

        payment.setOrderId(order.getId());
        paymentRepository.save(payment);

        OrderResponse orderResponse=new OrderResponse();
        orderResponse.setOrderTrackingNumber(order.getOrderTrackingNumber());
        orderResponse.setStatus(order.getStatus());
        orderResponse.setMessage("SUCCESS");

        return orderResponse;
    }
}
