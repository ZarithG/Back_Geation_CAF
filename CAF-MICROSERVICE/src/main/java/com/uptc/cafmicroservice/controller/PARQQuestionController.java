package com.uptc.cafmicroservice.controller;

import com.uptc.cafmicroservice.entity.PARQQuestion;
import com.uptc.cafmicroservice.service.PARQQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/caf/question")
public class PARQQuestionController {
    @Autowired
    private PARQQuestionService parqQuestionService;

    @GetMapping("/all")
    public ResponseEntity<List<PARQQuestion>> getAllPARQQuestions() {
        List<PARQQuestion> parqQuestionsList = parqQuestionService.getAll();
        if (parqQuestionsList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(parqQuestionsList);
    }
}
