package com.springteam.backend.service;

import com.springteam.backend.dto.CartDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailSenderService {
    private final String SUBJECT = "QUỐC CƯỜNG APPLE STORE XIN TRÂN TRỌNG CẢM ƠN";
    private JavaMailSender javaMailSender;

    @Autowired
    public MailSenderService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail( CartDto cart) {
        SimpleMailMessage message = new SimpleMailMessage();
        StringBuilder tableBuilder = new StringBuilder();
        tableBuilder.append("Danh sách mặt hàng đặt mua:\n");
        tableBuilder.append("Tên mặt hàng ").append(" | ").append(" Số lượng ").append(" | ").append(" Giá ").append("\n");
        tableBuilder.append("-".repeat(40)).append("\n");

        cart.getCartItem().forEach(item -> {
            tableBuilder.append(String.format("%-20s|%10d|%10d\n", item.getName(), item.getQuantity(), item.getTotalPrice()));
        });


        message.setFrom("2100009553@nttu.edu.vn");
        message.setTo(cart.getEmail());
        message.setSubject(SUBJECT);
        message.setText(tableBuilder.toString());


        javaMailSender.send(message);
    }
}
