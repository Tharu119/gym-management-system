package com.tharuka.gymmanagement.repository;

import com.tharuka.gymmanagement.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
}
