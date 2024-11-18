package com.uptc.cafmicroservice.controller;

import com.uptc.cafmicroservice.dto.PARQQuestionDTO;
import com.uptc.cafmicroservice.entity.PARQQuestion;
import com.uptc.cafmicroservice.service.InscriptionService;
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
    // Inyección de la dependencia de PARQQuestionService
    @Autowired
    private PARQQuestionService parqQuestionService;

    /**
     * Endpoint para obtener todas las preguntas PAR-Q.
     * @return Una ResponseEntity con la lista de preguntas PAR-Q o sin contenido si la lista está vacía.
     */
    @GetMapping("/all")
    public ResponseEntity<List<PARQQuestion>> getAllPARQQuestions() {
        // Llama al método del servicio para obtener la lista de preguntas PAR-Q
        List<PARQQuestion> parqQuestionsList = parqQuestionService.getAll();

        // Verifica si la lista está vacía y responde en consecuencia
        if (parqQuestionsList.isEmpty()) {
            return ResponseEntity.noContent().build(); // Devuelve un 204 No Content si no hay preguntas PAR-Q
        }

        // Devuelve un 200 OK con la lista de preguntas PAR-Q
        return ResponseEntity.ok(parqQuestionsList);
    }
}
