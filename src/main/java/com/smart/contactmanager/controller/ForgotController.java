package com.smart.contactmanager.controller;


import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.contactmanager.dao.OtpRepository;
import com.smart.contactmanager.dao.UserRepository;
import com.smart.contactmanager.entities.EmailRequest;
import com.smart.contactmanager.entities.Otp;
import com.smart.contactmanager.entities.User;
import com.smart.contactmanager.helper.Message;
import com.smart.contactmanager.service.EmailService;

@Controller
@RequestMapping("/forgotPassword")
public class ForgotController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping
    public String forgotPass(){

        return "forgotPass";
    }

    @PostMapping("/sendOTP")
    public String sendOTP(@RequestParam("email") String email,HttpSession session)
    {
        System.out.println("Email :" +email);

        User userEmail = this.userRepository.getUserByUsername(email); // returns null if user doesn't exists

        int otp = (int)(Math.random()*9000)+100;

        System.out.println("OTP"+otp);
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setTo(email);
        emailRequest.setSubject("OTP from SCM ");
        emailRequest.setBody("<h1> YOur OTP is "+otp);
        //session.setAttribute("otp", otp);  to verify otp by storing in session
        session.setAttribute("email",email);
        Boolean status = this.emailService.sendSimpleMail(emailRequest);

        //this.otpRepository.deleteById(this.otpRepository.findByUserEmail(email).getId());

        Otp otpObj = new Otp();
        otpObj.setUserEmail(email);
        otpObj.setSendOtp(String.valueOf(otp));
        this.otpRepository.save(otpObj);
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
        // String otpSent = session.getAttribute("otp").toString();  to verify by storing otp in session
        String userEmail = session.getAttribute("email").toString();
        Otp otpByUserEmail = this.otpRepository.findByUserEmail(userEmail);
        if(verifyOTP.equals(otpByUserEmail.getSendOtp()))
        {
            session.setAttribute("message", new Message("OTP Validated sucessfully", "alert-success"));
            return "changePassword";
        }
        else{
            session.setAttribute("message", new Message("Incorrect OTP", "alert-danger"));
            return "verifyOtp";
        }
        
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestParam("password")String password,HttpSession session){

        String userEmail = session.getAttribute("email").toString();
        User user = this.userRepository.getUserByUsername(userEmail);
        user.setPassword(bCryptPasswordEncoder.encode(password));

        this.userRepository.save(user);

        session.setAttribute("message", new Message("OTP Validated sucessfully", "alert-success"));
        
        return "redirect:/signin";
    }
}
