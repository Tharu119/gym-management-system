package com.tharuka.gymmanagement.repository;

import com.tharuka.gymmanagement.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByMemberId(Long memberId);
    List<Payment> findByStatus(String status);
    List<Payment> findByDueDateBeforeAndStatus(LocalDate date, String status);
}