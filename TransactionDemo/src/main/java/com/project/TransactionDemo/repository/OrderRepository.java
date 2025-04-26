package com.project.TransactionDemo.repository;

import com.project.TransactionDemo.entity.Order;
import com.project.TransactionDemo.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}

