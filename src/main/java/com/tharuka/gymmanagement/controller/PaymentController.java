package com.tharuka.gymmanagement.controller;

import com.tharuka.gymmanagement.entity.Member;
import com.tharuka.gymmanagement.entity.Payment;
import com.tharuka.gymmanagement.service.MemberService;
import com.tharuka.gymmanagement.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private MemberService memberService;

    @GetMapping("/member/{memberId}")
    public String getPaymentsByMember(@PathVariable Long memberId, Model model) {
        Member member = memberService.getMemberById(memberId);

        if (member == null) {
            System.out.println("Error: Member with ID " + memberId + " not found.");
            return "redirect:/error";
        }

        List<Payment> payments = paymentService.getPaymentsByMemberId(memberId);

        model.addAttribute("member", member);
        model.addAttribute("payments", payments);

        return "payment-status";
    }

    @PostMapping("/updateStatus/{paymentId}")
    public String updatePaymentStatus(@PathVariable Long paymentId) {
        Payment payment = paymentService.getPaymentById(paymentId);
        if (payment != null) {
            paymentService.processPayment(payment); // Process payment and update member dates
        }

        return "redirect:/payments/member/" + payment.getMember().getId();
    }
}
