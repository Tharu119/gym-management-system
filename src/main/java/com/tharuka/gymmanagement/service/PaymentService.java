package com.tharuka.gymmanagement.service;

import com.tharuka.gymmanagement.entity.Member;
import com.tharuka.gymmanagement.entity.Payment;
import com.tharuka.gymmanagement.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private MemberService memberService;

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Payment savePayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    public List<Payment> getPaymentsByMemberId(Long memberId) {
        return paymentRepository.findByMemberId(memberId);
    }

    public List<Payment> getPendingPayments() {
        return paymentRepository.findByStatus("Unpaid");
    }

    public List<Payment> getOverduePayments() {
        LocalDate today = LocalDate.now();
        return paymentRepository.findByDueDateBeforeAndStatus(today, "Unpaid");
    }

    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id).orElse(null);
    }

    @Scheduled(cron = "0 0 0 * * ?") // Run daily at midnight
    public void checkAndUpdateMemberStatus() {
        LocalDate today = LocalDate.now();
        List<Payment> overduePayments = getOverduePayments();

        for (Payment payment : overduePayments) {
            Member member = payment.getMember();
            if (member != null) {
                member.setStatus("Inactive");
                memberService.saveMember(member);
            }
        }
    }

    public void processPayment(Payment payment) {
        payment.setStatus("Paid");
        paymentRepository.save(payment);

        Member member = payment.getMember();
        if (member != null) {
            // Update the member status to Active if the payment is made
            member.setStatus("Active");

            LocalDate currentEndDate = member.getEndDate();
            LocalDate today = LocalDate.now();

            if (today.isAfter(currentEndDate)) {
                // The payment is done after the end date, set the start date to today
                member.setStartDate(today);
            } else {
                // Otherwise, continue from the current end date
                member.setStartDate(currentEndDate);
            }

            // Update the end date based on the plan type
            switch (member.getPlanType()) {
                case "Daily":
                    member.setEndDate(member.getStartDate().plusDays(1));
                    break;
                case "Monthly":
                    member.setEndDate(member.getStartDate().plusMonths(1));
                    break;
                case "3-Month":
                    member.setEndDate(member.getStartDate().plusMonths(3));
                    break;
                case "6-Month":
                    member.setEndDate(member.getStartDate().plusMonths(6));
                    break;
                case "12-Month":
                    member.setEndDate(member.getStartDate().plusMonths(12));
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + member.getPlanType());
            }

            memberService.saveMember(member);
        }
    }

}



