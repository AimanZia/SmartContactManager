package com.smart.contactmanager.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.smart.contactmanager.dao.ContactRepository;
import com.smart.contactmanager.dao.UserRepository;
import com.smart.contactmanager.entities.Contact;
import com.smart.contactmanager.entities.User;

@RestController
public class SearchController {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired 
    private UserRepository userRepository;

    //search Handler 
    @GetMapping("/search/{query}")
    public ResponseEntity<?> searchHandler(@PathVariable("query") String query,Principal principal)
    {
        System.out.println(query);
        User userByUsername = this.userRepository.getUserByUsername(principal.getName());
        List<Contact> findByNameContainingAndUser = this.contactRepository.findByNameContainingAndUser(query, userByUsername);

        return ResponseEntity.ok(findByNameContainingAndUser);
    }
    
}
