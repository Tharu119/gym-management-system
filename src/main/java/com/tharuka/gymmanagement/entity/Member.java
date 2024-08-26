package com.tharuka.gymmanagement.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "members")
@Data
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String contact;
    private String address;
    private String planType;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status; // Active/Inactive
    private BigDecimal amount; // Plan amount
    private LocalDate joiningDate; // New field for joining date (same as startDate)

    @Transient // This field is not stored in the database
    private String paymentStatus; // Payment Status (Paid/Not Paid)
}


