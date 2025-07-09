package com.frank.paymentservice.service;

import com.frank.paymentservice.clients.OrderClient;
import com.frank.paymentservice.dto.PaymentRequest;
import com.frank.paymentservice.dto.PaymentResponse;
import com.frank.paymentservice.dto.order.OrderDTO;
import com.frank.paymentservice.dto.order.OrderStatus;
import com.frank.paymentservice.entity.Payment;
import com.frank.paymentservice.entity.PaymentStatus;
import com.frank.paymentservice.exception.OrderException;
import com.frank.paymentservice.exception.PaymentNotFoundException;
import com.frank.paymentservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderClient orderClient;
    private Boolean lastPaymentSuccess = null;

    private Payment findById(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found with ID: " + paymentId));
    }

    public PaymentResponse processPayment(PaymentRequest request) {
        // 1. Validate existing order
        OrderDTO order = orderClient.getOrderById(request.getOrderId());
        if (order == null) {
            throw new OrderException("Order not found");
        }

        // Check if order is already paid
        if (order.getStatus() == OrderStatus.PAID) {
            throw new OrderException("Order is already paid. Cannot process another payment.");
        }

        // 2. Simulate successful or failed payment logic
        boolean paymentSuccess;
        if (lastPaymentSuccess == null) {
            paymentSuccess = new java.util.Random().nextBoolean();
        } else {
            paymentSuccess = !lastPaymentSuccess;
        }
        lastPaymentSuccess = paymentSuccess;

        // 3. Create Payment entity
        Payment payment = Payment.builder()
                .orderId(request.getOrderId())
                .amount(request.getAmount())
                .status(paymentSuccess ? PaymentStatus.COMPLETED : PaymentStatus.FAILED)
                .build();

        paymentRepository.save(payment);

        // 4. Update order according to result
        if (paymentSuccess) {
            orderClient.updateOrderStatus(request.getOrderId(), OrderStatus.PAID);
        } else {
            orderClient.updateOrderStatus(request.getOrderId(), OrderStatus.FAILED);
        }

        // 5. Return response
        return PaymentResponse.builder()
                .id(payment.getId())
                .orderId(payment.getOrderId())
                .amount(payment.getAmount())
                .status(payment.getStatus())
                .paymentDate(payment.getPaymentDate())
                .build();
    }

    public List<PaymentResponse> getAllPayments() {
        List<Payment> payments = paymentRepository.findAll();
        return payments.stream()
                .map(payment -> PaymentResponse.builder()
                        .id(payment.getId())
                        .orderId(payment.getOrderId())
                        .amount(payment.getAmount())
                        .status(payment.getStatus())
                        .paymentDate(payment.getPaymentDate())
                        .build())
                .toList();
    }

    public PaymentResponse getPaymentDetail(Long paymentId) {
        Payment payment = findById(paymentId);

        return PaymentResponse.builder()
                .id(payment.getId())
                .orderId(payment.getOrderId())
                .amount(payment.getAmount())
                .status(payment.getStatus())
                .paymentDate(payment.getPaymentDate())
                .build();
    }
}