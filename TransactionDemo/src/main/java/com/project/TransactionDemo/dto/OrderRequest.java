package com.project.TransactionDemo.dto;

import com.project.TransactionDemo.entity.Order;
import com.project.TransactionDemo.entity.Payment;

public class OrderRequest {
    private Order order;
    private Payment payment;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}
