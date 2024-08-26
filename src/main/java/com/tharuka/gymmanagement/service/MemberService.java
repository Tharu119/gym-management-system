package com.tharuka.gymmanagement.service;

import com.tharuka.gymmanagement.entity.Member;
import com.tharuka.gymmanagement.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Member saveMember(Member member) {
        return memberRepository.save(member);
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public Member getMemberById(Long id) {
        return memberRepository.findById(id).orElse(null);
    }

    public List<Member> searchMembers(String name, String joiningDate, String contact) {
        if ((name != null && !name.isEmpty()) && (joiningDate == null || joiningDate.isEmpty()) && (contact == null || contact.isEmpty())) {
            return memberRepository.findByNameContainingIgnoreCase(name);
        } else if ((joiningDate != null && !joiningDate.isEmpty()) && (name == null || name.isEmpty()) && (contact == null || contact.isEmpty())) {
            LocalDate date = LocalDate.parse(joiningDate);
            return memberRepository.findByJoiningDate(date);
        } else if ((contact != null && !contact.isEmpty()) && (name == null || name.isEmpty()) && (joiningDate == null || joiningDate.isEmpty())) {
            return memberRepository.findByContactContaining(contact);
        } else if ((name != null && !name.isEmpty()) && (joiningDate != null && !joiningDate.isEmpty()) && (contact == null || contact.isEmpty())) {
            LocalDate date = LocalDate.parse(joiningDate);
            return memberRepository.findByNameContainingIgnoreCaseAndJoiningDate(name, date);
        } else if ((name != null && !name.isEmpty()) && (contact != null && !contact.isEmpty()) && (joiningDate == null || joiningDate.isEmpty())) {
            return memberRepository.findByNameContainingIgnoreCaseAndContactContaining(name, contact);
        } else if ((contact != null && !contact.isEmpty()) && (joiningDate != null && !joiningDate.isEmpty()) && (name == null || name.isEmpty())) {
            LocalDate date = LocalDate.parse(joiningDate);
            return memberRepository.findByContactContainingAndJoiningDate(contact, date);
        } else if ((name != null && !name.isEmpty()) && (joiningDate != null && !joiningDate.isEmpty()) && (contact != null && !contact.isEmpty())) {
            LocalDate date = LocalDate.parse(joiningDate);
            return memberRepository.findByNameContainingIgnoreCaseAndJoiningDateAndContactContaining(name, date, contact);
        } else {
            return getAllMembers();
        }
    }

    public void deleteMemberById(Long id) {
        memberRepository.deleteById(id);
    }
}

