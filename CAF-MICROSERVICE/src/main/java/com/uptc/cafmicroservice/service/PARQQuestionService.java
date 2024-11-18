package com.uptc.cafmicroservice.service;

import com.uptc.cafmicroservice.dto.UserResponseDTO;
import com.uptc.cafmicroservice.entity.Inscription;
import com.uptc.cafmicroservice.entity.PARQQuestion;
import com.uptc.cafmicroservice.entity.UserResponse;
import com.uptc.cafmicroservice.mapper.PARQQuestionMapper;
import com.uptc.cafmicroservice.repository.PARQQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class PARQQuestionService {
    // Inyección del repositorio de preguntas PAR-Q para acceder a la base de datos
    @Autowired
    PARQQuestionRepository parqQuestionRepository;

    // Inyección de RestTemplate para realizar llamadas a otros microservicios si es necesario
    @Autowired
    RestTemplate restTemplate;

    /**
     * Recupera todas las preguntas PAR-Q almacenadas en la base de datos.
     * @return Lista de objetos PARQQuestion.
     */
    public List<PARQQuestion> getAll() {
        return parqQuestionRepository.findAll(); // Devuelve todas las preguntas desde el repositorio
    }
}
