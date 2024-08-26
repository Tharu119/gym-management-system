package com.tharuka.gymmanagement.repository;

import com.tharuka.gymmanagement.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<Plan, Long> {
}
