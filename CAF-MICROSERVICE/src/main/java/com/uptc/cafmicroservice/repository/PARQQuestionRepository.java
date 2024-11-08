package com.uptc.cafmicroservice.repository;

import com.uptc.cafmicroservice.entity.PARQQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PARQQuestionRepository extends JpaRepository<PARQQuestion, Integer> {
}
