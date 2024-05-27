package com.tms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tms.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
   
}
