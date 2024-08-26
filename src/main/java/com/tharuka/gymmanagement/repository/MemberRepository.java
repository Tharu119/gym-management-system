package com.tharuka.gymmanagement.repository;

import com.tharuka.gymmanagement.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByNameContainingIgnoreCase(String name);

    List<Member> findByJoiningDate(LocalDate joiningDate);

    List<Member> findByContactContaining(String contact);

    List<Member> findByNameContainingIgnoreCaseAndJoiningDate(String name, LocalDate joiningDate);

    List<Member> findByNameContainingIgnoreCaseAndContactContaining(String name, String contact);

    List<Member> findByContactContainingAndJoiningDate(String contact, LocalDate joiningDate);

    List<Member> findByNameContainingIgnoreCaseAndJoiningDateAndContactContaining(String name, LocalDate joiningDate, String contact);
}
