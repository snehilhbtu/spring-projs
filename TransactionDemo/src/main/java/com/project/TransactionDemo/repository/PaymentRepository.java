package com.project.TransactionDemo.repository;

import com.project.TransactionDemo.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
}
