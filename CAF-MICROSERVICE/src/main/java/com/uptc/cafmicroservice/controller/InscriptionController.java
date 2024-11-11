package com.uptc.cafmicroservice.controller;

import com.uptc.cafmicroservice.dto.InscriptionDTO;
import com.uptc.cafmicroservice.entity.Inscription;
import com.uptc.cafmicroservice.service.InscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/caf/inscription")
public class InscriptionController {
    @Autowired
    InscriptionService inscriptionService;

    @PostMapping("/inscribe-user")
    public ResponseEntity<InscriptionDTO> getAllPARQQuestions(@RequestBody InscriptionDTO inscriptionDTO) {
        InscriptionDTO inscriptionDTOResponse = inscriptionService.inscribeUserInFitnessCenter(inscriptionDTO);
        if (inscriptionDTOResponse == null) {
            return ResponseEntity.noContent().build();
        }
//        InscriptionDTO inscriptionDTOResponse = new InscriptionDTO();
//        inscriptionDTOResponse.setId(inscription.getId());
//        inscriptionDTOResponse.setInscriptionDate(inscription.getInscriptionDate());
//        inscriptionDTOResponse.setInscriptionStatus(inscription.getInscriptionStatus());

        return ResponseEntity.ok(inscriptionDTOResponse);
    }
}
