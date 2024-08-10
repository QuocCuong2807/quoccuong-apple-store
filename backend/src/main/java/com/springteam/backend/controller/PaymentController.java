package com.springteam.backend.controller;

import com.springteam.backend.config.VNPayConfig;
import com.springteam.backend.dto.CartDto;
import com.springteam.backend.dto.PaymentResponse;
import com.springteam.backend.service.IOrderService;
import com.springteam.backend.service.IPaymentService;
import com.springteam.backend.service.MailSenderService;
import com.springteam.backend.service.PaymentServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/payment")
public class PaymentController {
    private IOrderService orderService;
    private MailSenderService mailSenderService;
    private IPaymentService paymentService;

    @Autowired
    public PaymentController(IOrderService orderService, MailSenderService mailSenderService, IPaymentService paymentService) {
        this.orderService = orderService;
        this.mailSenderService = mailSenderService;
        this.paymentService = paymentService;
    }

    @PostMapping("/cod-payment")
    public ResponseEntity<String> saveOrder(@RequestBody CartDto cart,
                                            @RequestParam String province,
                                            @RequestParam String district,
                                            @RequestParam String ward) {

        //end handle vnp_response
        orderService.saveOrder(cart, province, district, ward);
        mailSenderService.sendEmail(cart);
        return new ResponseEntity<>("success", HttpStatus.CREATED);
    }


    @PostMapping("/vnp-payment")
    public ResponseEntity<String> saveVNPOrder(@RequestBody CartDto cart,
                                               @RequestParam @NotNull @NotBlank String province,
                                               @RequestParam @NotNull @NotBlank String district,
                                               @RequestParam @NotNull @NotBlank String ward,
                                               @RequestParam @NotNull @NotBlank String vnp_ResponseCode) {
        System.out.println(vnp_ResponseCode);
        if (vnp_ResponseCode.equals("00")) {
            orderService.saveOrder(cart, province, district, ward);
            mailSenderService.sendEmail(cart);
            return new ResponseEntity<>("success", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Có lỗi trong quá trình thanh toán", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/vnp-order-creation")
    public ResponseEntity<PaymentResponse> createOrderPayment(HttpServletRequest req,
                                                              @RequestParam String amount,
                                                              @RequestParam String txt_billing_mobile,
                                                              @RequestParam String txt_billing_email,
                                                              @RequestParam String bill_fullName,
                                                              @RequestParam String province,
                                                              @RequestParam String district,
                                                              @RequestParam String ward,
                                                              @RequestParam String address) throws UnsupportedEncodingException {
        PaymentResponse paymentResponse = paymentService.vnpCreateOrder(req, amount, txt_billing_mobile, txt_billing_email
                , bill_fullName, province, district, ward, address);

        return new ResponseEntity<>(paymentResponse, HttpStatus.CREATED);
    }
}


