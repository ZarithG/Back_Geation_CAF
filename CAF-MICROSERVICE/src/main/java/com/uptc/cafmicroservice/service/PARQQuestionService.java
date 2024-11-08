package com.uptc.cafmicroservice.service;

import com.uptc.cafmicroservice.entity.PARQQuestion;
import com.uptc.cafmicroservice.repository.PARQQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class PARQQuestionService {
    @Autowired
    PARQQuestionRepository parqQuestionRepository;

    @Autowired
    RestTemplate restTemplate;

    public List<PARQQuestion> getAll(){
        return parqQuestionRepository.findAll();
    }
}
