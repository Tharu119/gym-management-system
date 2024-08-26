package com.tharuka.gymmanagement.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "payments")
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private LocalDate paymentDate; // Start date as payment date
    private LocalDate dueDate; // Halfway through the plan duration as due date
    private BigDecimal amount; // Plan amount
    private String status; // Paid/Not Paid
}
