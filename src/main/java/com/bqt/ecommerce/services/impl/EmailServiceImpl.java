package com.bqt.ecommerce.services.impl;

import com.bqt.ecommerce.entities.EmailDetails;
import com.bqt.ecommerce.services.IEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements IEmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}") private String sender;

    @Override
    public String sendSimpleMail(EmailDetails details) {

        try{
            // Creating a simple mail message
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

            // Setting up necessary details
            simpleMailMessage.setFrom(sender);
            simpleMailMessage.setTo(details.getRecipient());
            simpleMailMessage.setText(details.getMsgBody());
            simpleMailMessage.setSubject(details.getSubject());

            // Sending the mail
            javaMailSender.send(simpleMailMessage);
            return "Mail Sent Successfully...";
        } catch (Exception e){
            return "Error while Sending Mail";
        }
    }

}
