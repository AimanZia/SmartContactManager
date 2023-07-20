package com.smart.contactmanager.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.contactmanager.dao.UserRepository;
import com.smart.contactmanager.entities.User;
import com.smart.contactmanager.helper.Message;

@Controller
public class HomeController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @GetMapping("/")
    public String testCont(Model model){
        model.addAttribute("title", "Home Contact");
        return "home";
    }

    @GetMapping("/signup")
    public String signup(Model model){
      model.addAttribute("title", "Register");
      model.addAttribute("user", new User());
      return "signup";
    }

    @PostMapping("/signuphere")
    public String register(@ModelAttribute("user")User user ,BindingResult result1 , @RequestParam(value = "acceptTandC",defaultValue = "false")boolean agreement
    ,Model model, HttpSession session)
    {
      System.out.println(agreement);
      System.out.println(result1);
      
      try {
        if(!agreement)
        {
          throw new Exception(" Accept Terms and Condition");
        }
        if(result1.hasErrors())
        {
          model.addAttribute("user", user);
          System.out.println("Error -"+result1.toString());
          return "signup";
        }
        user.setRole("ROLE_USER");
        user.setEnabled(true);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        User result = this.userRepository.save(user);
        System.out.println(result);
        model.addAttribute("user",new User());
        session.setAttribute("message", new Message("Sucess","alert-success"));
        return "signup";
      } catch (Exception e) {
        e.printStackTrace();
        model.addAttribute("user", user);
        session.setAttribute("message", new Message("Something went wrong "+e.getMessage(), "alert-danger"));
        return "signup";
      }
      
    }

    @GetMapping("/signin")
    public String customSignIn(Model model){
      model.addAttribute("title", "Sign In - smart ");
      return "signin";
    }

    @GetMapping("/adminSignup")
    public String adminRegisterPage(Model model)
    {
      model.addAttribute("title", "Register");
      model.addAttribute("user", new User());
      return "adminSignup";
    }

    @PostMapping("/adminSignup")
    public String adminSignup(@ModelAttribute("user") User user , BindingResult result,@RequestParam(value = "acceptTandC",defaultValue = "false")
    boolean agreement,ModelMap model, HttpSession session)
    {
      try {
        if(!agreement)
        {
          throw new Exception("Accept Terms and Condition");
        }
        if(result.hasErrors())
        {
          model.addAttribute("user", user);
          System.out.println(result.toString());
          return "adminSignup";
        }
        user.setRole("ROLE_ADMIN");
        user.setEnabled(true);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        User savedUser = this.userRepository.save(user);
        model.addAttribute("user", new User());
        session.setAttribute("message", new Message("Sucess","alert-success"));
        return "adminSignup";
      } catch (Exception e) {
        e.printStackTrace();
        session.setAttribute("message", new Message("Something went Wrong"+e.getMessage() ,"alert-danger"));
        return "adminSignup";
      }
      
    }
}
