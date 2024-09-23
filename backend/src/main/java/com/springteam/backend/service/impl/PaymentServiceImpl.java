package com.springteam.backend.service.impl;

import com.springteam.backend.config.VNPayConfig;
import com.springteam.backend.dto.PaymentResponse;
import com.springteam.backend.service.IPaymentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PaymentServiceImpl implements IPaymentService {
    private VNPayConfig vnPayConfig;

    @Autowired
    public PaymentServiceImpl(VNPayConfig vnPayConfig) {
        this.vnPayConfig = vnPayConfig;
    }

    @Override
    public PaymentResponse vnpCreateOrder(HttpServletRequest req, String amount, String txt_billing_mobile, String txt_billing_email, String bill_fullName, String province, String district, String ward, String address) throws UnsupportedEncodingException {

        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "topup";
        String vnp_OrderInfo = vnPayConfig.getVnp_OrderInfo();
        String vnp_TxnRef = vnPayConfig.getRandomNumber(8);
        String vnp_IpAddr = vnPayConfig.getIpAddress(req);
        String vnp_TmnCode = vnPayConfig.getVnp_TmnCode();
        //gia tien san pham
        long vnp_totalPrice = Long.parseLong(amount) * 100;
        System.out.println(vnp_totalPrice);
        Map vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", vnp_totalPrice);
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", "ncb");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", "vn");

        vnp_Params.put("vnp_ReturnUrl", vnPayConfig.getVnp_ReturnUrl());
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());

        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        //Add Params of 2.1.0 Version
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        vnp_Params.put("vnp_Bill_Mobile", txt_billing_mobile);
        vnp_Params.put("vnp_Bill_Email", txt_billing_email);
        vnp_Params.put("vnp_Bill_FirstName", bill_fullName);
        vnp_Params.put("vnp_Bill_LastName", bill_fullName);

        StringBuilder addressStringBuilder = new StringBuilder();
        addressStringBuilder.append(address);
        addressStringBuilder.append(", ");
        addressStringBuilder.append(province);
        addressStringBuilder.append(", ");
        addressStringBuilder.append(district);
        addressStringBuilder.append(", ");
        addressStringBuilder.append(ward);

        vnp_Params.put("vnp_Bill_Address", addressStringBuilder.toString());
        vnp_Params.put("vnp_Bill_City", province);
        vnp_Params.put("vnp_Bill_Country", vnPayConfig.getTxt_bill_country());
        vnp_Params.put("vnp_Inv_Phone", vnPayConfig.getVnp_Inv_Phone());
        vnp_Params.put("vnp_Inv_Email", txt_billing_email);
        vnp_Params.put("vnp_Inv_Address", vnPayConfig.getTxt_inv_addr1());

        //Build data to hash and querystring
        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            System.out.println(fieldName);
            vnp_Params.forEach((key, value) -> System.out.println(key +" : " + value));
            String fieldValue = vnp_Params.get(fieldName).toString();
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = vnPayConfig.hmacSHA512(vnPayConfig.getSecretKey(), hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;

        PaymentResponse paymentResponse = PaymentResponse.builder().code("00").message("success").data(paymentUrl).build();

        return paymentResponse;
    }
}
