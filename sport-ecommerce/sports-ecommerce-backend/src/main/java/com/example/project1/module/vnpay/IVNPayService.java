package com.example.project1.module.vnpay;

import com.example.project1.model.dto.payment.PaymentDTO;
import com.example.project1.model.dto.payment.PaymentQueryDTO;
import com.example.project1.model.dto.payment.PaymentRefundDTO;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public interface IVNPayService {
    String createPaymentUrl(PaymentDTO paymentRequest, HttpServletRequest request);
    String queryTransaction(PaymentQueryDTO paymentQueryDTO, HttpServletRequest request) throws IOException;
    String refundTransaction(PaymentRefundDTO refundDTO) throws IOException;
}
