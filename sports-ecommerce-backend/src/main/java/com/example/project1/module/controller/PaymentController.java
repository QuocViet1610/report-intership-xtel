package com.example.project1.module.controller;

import com.example.project1.model.dto.ResponseResult;
import com.example.project1.model.dto.payment.PaymentDTO;
import com.example.project1.model.dto.payment.PaymentQueryDTO;
import com.example.project1.model.dto.payment.PaymentRefundDTO;
import com.example.project1.module.vnpay.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final VNPayService vnPayService;

    @PostMapping("/create_payment_url")
    public ResponseResult<String> createPayment(@RequestBody PaymentDTO paymentRequest, HttpServletRequest request) {
        try {
            String paymentUrl = vnPayService.createPaymentUrl(paymentRequest, request);
            return ResponseResult.ofSuccess(paymentUrl);
        } catch (Exception e) {
            return ResponseResult.ofFail("Error generating payment URL: " + e.getMessage());
        }
    }

    @PostMapping("/query")
    public ResponseResult<String> queryTransaction(@RequestBody PaymentQueryDTO paymentQueryDTO, HttpServletRequest request) {
        try {
            String result = vnPayService.queryTransaction(paymentQueryDTO, request);
            return ResponseResult.ofSuccess(result + "Query successful");
        } catch (Exception e) {
            return ResponseResult.ofFail("Error querying transaction: " + e.getMessage());
        }
    }

    @PostMapping("/refund")
    public ResponseResult<String> refundTransaction(@RequestBody PaymentRefundDTO paymentRefundDTO) {
        try {
            String response = vnPayService.refundTransaction(paymentRefundDTO);
            return ResponseResult.ofSuccess(response+ "Refund processed successfully");
        } catch (Exception e) {
            return ResponseResult.ofFail("Failed to process refund: " + e.getMessage());
        }
    }
}

