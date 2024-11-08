package com.uptc.cafmicroservice.repository;

import com.uptc.cafmicroservice.entity.UserResponse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserResponseRepository extends JpaRepository<UserResponse, Integer> {
}
