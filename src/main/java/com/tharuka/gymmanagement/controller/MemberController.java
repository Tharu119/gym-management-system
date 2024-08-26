package com.tharuka.gymmanagement.controller;

import com.tharuka.gymmanagement.entity.Member;
import com.tharuka.gymmanagement.entity.Payment;
import com.tharuka.gymmanagement.service.MemberService;
import com.tharuka.gymmanagement.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/members/create")
    public String showCreateMemberForm(Model model) {
        model.addAttribute("member", new Member());
        return "create-member";
    }

    @PostMapping("/api/members")
    public String createMember(@ModelAttribute Member member) {
        // Set end date based on planType
        if ("Daily".equals(member.getPlanType())) {
            member.setEndDate(member.getStartDate().plusDays(1));
        } else if ("Monthly".equals(member.getPlanType())) {
            member.setEndDate(member.getStartDate().plusMonths(1));
        } else if ("3-Month".equals(member.getPlanType())) {
            member.setEndDate(member.getStartDate().plusMonths(3));
        } else if ("6-Month".equals(member.getPlanType())) {
            member.setEndDate(member.getStartDate().plusMonths(6));
        } else if ("12-Month".equals(member.getPlanType())) {
            member.setEndDate(member.getStartDate().plusMonths(12));
        }

        // Save member
        memberService.saveMember(member);

        // Create Payment details
        Payment payment = new Payment();
        payment.setMember(member);
        payment.setPaymentDate(member.getStartDate());
        payment.setAmount(member.getAmount());

        // Calculate due date (halfway through the plan duration)
        LocalDate dueDate = member.getStartDate();
        if ("Daily".equals(member.getPlanType())) {
            dueDate = member.getStartDate(); // No due date for daily plans
        } else if ("Monthly".equals(member.getPlanType())) {
            dueDate = member.getStartDate().plusWeeks(2);
        } else if ("3-Month".equals(member.getPlanType())) {
            dueDate = member.getStartDate().plusMonths(1).plusDays(15);
        } else if ("6-Month".equals(member.getPlanType())) {
            dueDate = member.getStartDate().plusMonths(3);
        } else if ("12-Month".equals(member.getPlanType())) {
            dueDate = member.getStartDate().plusMonths(6);
        }

        payment.setDueDate(dueDate);
        payment.setStatus("Not Paid"); // Initial status is Not Paid

        paymentService.savePayment(payment);

        return "redirect:/members";
    }

    @GetMapping("/members")
    public String listMembers(@RequestParam(required = false) String name,
                              @RequestParam(required = false) String joiningDate,
                              @RequestParam(required = false) String contact,
                              Model model) {
        List<Member> members;

        if ((name != null && !name.isEmpty()) || (joiningDate != null && !joiningDate.isEmpty()) || (contact != null && !contact.isEmpty())) {
            members = memberService.searchMembers(name, joiningDate, contact);
        } else {
            members = memberService.getAllMembers();
        }

        // Calculate payment status for each member
        for (Member member : members) {
            List<Payment> payments = paymentService.getPaymentsByMemberId(member.getId());
            if (payments.isEmpty()) {
                member.setPaymentStatus("Not Paid");
            } else {
                Payment latestPayment = payments.get(payments.size() - 1); // Assuming the latest payment is the last one
                member.setPaymentStatus(latestPayment.getStatus());
            }

            // Check if the member's plan should be renewed
            LocalDate today = LocalDate.now();
            if (today.isAfter(member.getEndDate())) {
                Payment lastPayment = payments.get(payments.size() - 1);

                if (lastPayment != null && "Paid".equals(lastPayment.getStatus())) {
                    paymentService.processPayment(lastPayment); // Auto-renew the plan
                }
            }
        }

        model.addAttribute("members", members);
        return "members";
    }


    @GetMapping("/payments")
    public String listPayments(Model model) {
        model.addAttribute("payments", paymentService.getAllPayments());
        return "payments";
    }

    @GetMapping("/members/{memberId}/profile")
    public String viewMemberProfile(@PathVariable Long memberId, Model model) {
        Member member = memberService.getMemberById(memberId);

        if (member == null) {
            return "redirect:/members"; // Redirect to members list if member not found
        }

        model.addAttribute("member", member);
        return "member-profile";
    }

    @PostMapping("/members/{memberId}/update")
    public String updateMemberProfile(@PathVariable Long memberId, @ModelAttribute Member member) {
        Member existingMember = memberService.getMemberById(memberId);

        if (existingMember != null) {
            existingMember.setContact(member.getContact());
            existingMember.setAddress(member.getAddress());
            existingMember.setPlanType(member.getPlanType());

            // Update endDate based on planType
            if ("Daily".equals(member.getPlanType())) {
                existingMember.setEndDate(existingMember.getStartDate().plusDays(1));
            } else if ("Monthly".equals(member.getPlanType())) {
                existingMember.setEndDate(existingMember.getStartDate().plusMonths(1));
            } else if ("3-Month".equals(member.getPlanType())) {
                existingMember.setEndDate(existingMember.getStartDate().plusMonths(3));
            } else if ("6-Month".equals(member.getPlanType())) {
                existingMember.setEndDate(existingMember.getStartDate().plusMonths(6));
            } else if ("12-Month".equals(member.getPlanType())) {
                existingMember.setEndDate(existingMember.getStartDate().plusMonths(12));
            }

            memberService.saveMember(existingMember);
        }

        return "redirect:/members/" + memberId + "/profile";
    }

    @PostMapping("/members/delete/{id}")
    public String deleteMember(@PathVariable Long id) {
        memberService.deleteMemberById(id);
        return "redirect:/members";
    }


}
