package com.smart.contactmanager.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class CustomErrorController implements ErrorController{

    @GetMapping
    public String handleError(HttpServletRequest request,ModelMap model)
    {
         Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
         Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);

         if(status !=null)
         {
            Integer statusCode = Integer.valueOf(status.toString());

            if(statusCode == HttpStatus.NOT_FOUND.value())
            {
                model.addAttribute("statusCode", statusCode);
            }
            else if(statusCode == HttpStatus.BAD_REQUEST.value())
            {
                model.addAttribute("statusCode", statusCode);
            }
            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value())
            {
                model.addAttribute("statusCode", statusCode);
            }
            else if(statusCode == HttpStatus.FORBIDDEN.value())
            {
                model.addAttribute("statusCode", statusCode);
            }
            else 
            {
                model.addAttribute("statusCode", "Something Went wrong");
            }

            System.out.println(message);
         }

         return "errorPage";
    }
    
}
