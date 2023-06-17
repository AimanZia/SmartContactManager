package com.smart.contactmanager.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smart.contactmanager.entities.Otp;

public interface OtpRepository extends JpaRepository<Otp,Integer>{

    Otp findByUserEmail(String userEmail);
    
}
