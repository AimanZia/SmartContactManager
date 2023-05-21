package com.smart.contactmanager.controller;


import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.contactmanager.entities.EmailRequest;
import com.smart.contactmanager.helper.Message;
import com.smart.contactmanager.service.EmailService;

@Controller
@RequestMapping("/forgotPassword")
public class ForgotController {

    @Autowired
    private EmailService emailService;

    @GetMapping
    public String forgotPass(){

        return "forgotPass";
    }

    @PostMapping("/sendOTP")
    public String sendOTP(@RequestParam("email") String email,HttpSession session)
    {
        System.out.println("Email :" +email);

        int otp = (int)(Math.random()*9000)+100;

        System.out.println("OTP"+otp);
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setTo(email);
        emailRequest.setSubject("OTP from SCM ");
        emailRequest.setBody("<h1> YOur OTP is "+otp);
        session.setAttribute("otp", otp);
        Boolean status = this.emailService.sendSimpleMail(emailRequest);
        if(status)
        {
            return "verifyOtp";
        }
        else{
            return "redirect:/forgotPassword";
        }
      
    }

    @PostMapping("/verifyOTP")
    public String verifyOTP(@RequestParam("otp") String verifyOTP,HttpSession session){
        String otpSent = session.getAttribute("otp").toString();
        if(verifyOTP.equals(otpSent))
        {
            session.setAttribute("message", new Message("OTP Validated sucessfully", "alert-success"));
            return "verifyOtp";
        }
        else{
            session.setAttribute("message", new Message("Incorrect OTP", "alert-danger"));
            return "verifyOtp";
        }
        
    }
}
