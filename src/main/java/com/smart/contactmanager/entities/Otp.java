package com.smart.contactmanager.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name= "otp")
public class Otp {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String sendOtp;
    private String validateOtp;
    private String userEmail;


    public Otp() {
    }
    
    public int getId() {
        return id;
    }
  
  public void setId(int id) {
        this.id = id;
    }
    public String getSendOtp() {
        return sendOtp;
    }
    public void setSendOtp(String sendOtp) {
        this.sendOtp = sendOtp;
    }
    public String getValidateOtp() {
        return validateOtp;
    }
    public void setValidateOtp(String validateOtp) {
        this.validateOtp = validateOtp;
    }
    public String getUserEmail() {
        return userEmail;
    }
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @Override
    public String toString() {
        return "Otp [id=" + id + ", sendOtp=" + sendOtp + ", validateOtp=" + validateOtp + ", userEmail=" + userEmail
                + "]";
    }

    
}
