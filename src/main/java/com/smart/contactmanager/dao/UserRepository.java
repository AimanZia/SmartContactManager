package com.smart.contactmanager.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smart.contactmanager.entities.User;

public interface UserRepository extends JpaRepository<User,Integer>{
    
    @Query("select u from User u where u.email =:emailUser")               //method to get User by email @Param and after colon should be same
    public User getUserByUsername(@Param("emailUser") String email);
}
