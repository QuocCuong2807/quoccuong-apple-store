package com.springteam.backend.service;

import com.springteam.backend.dto.PaymentResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;

public interface IPaymentService {
    PaymentResponse vnpCreateOrder(HttpServletRequest req,
                                   String amount,
                                   String txt_billing_mobile,
                                   String txt_billing_email,
                                   String bill_fullName,
                                   String province,
                                   String district,
                                   String ward,
                                   String address) throws UnsupportedEncodingException;
}
