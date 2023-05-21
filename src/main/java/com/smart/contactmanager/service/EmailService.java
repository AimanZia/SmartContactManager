package com.smart.contactmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.smart.contactmanager.entities.EmailRequest;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;
    
    @Value("${spring.mail.username}")
    private String sender;

    public Boolean sendSimpleMail(EmailRequest emailRequest)
    {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            
            simpleMailMessage.setFrom(sender);
            simpleMailMessage.setTo(emailRequest.getTo());
            simpleMailMessage.setText(emailRequest.getBody());
            simpleMailMessage.setSubject(emailRequest.getSubject());

            javaMailSender.send(simpleMailMessage);

            return true;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            System.out.println(e);
            return false;
        }
    }

}
