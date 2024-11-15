package com.uptc.cafmicroservice.controller;

import com.uptc.cafmicroservice.dto.InscriptionDTO;
import com.uptc.cafmicroservice.entity.Inscription;
import com.uptc.cafmicroservice.enums.ConsentTypeEnum;
import com.uptc.cafmicroservice.enums.InscriptionStatusEnum;
import com.uptc.cafmicroservice.service.InscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/caf/inscription")
public class InscriptionController {
    @Autowired
    InscriptionService inscriptionService;

//    @PostMapping("/test-inscribe")
//    public String testInscribe(@RequestParam("files") MultipartFile[] multipartFiles, @RequestParam("type") ConsentTypeEnum consentType) {
//        System.out.println("LLEGO INSCRIPCIÓN" + multipartFiles.length + " - " + consentType);
//        return "Test";
//    }

    @PostMapping("/test-inscribe")
    public String testInscribe(@RequestParam("files") MultipartFile[] multipartFiles, @RequestParam("type") ConsentTypeEnum consentType) {
        System.out.println("LLEGO INSCRIPCIÓN" + multipartFiles.length + " - " + consentType);
        return "Test";
    }

    @PostMapping("/inscribe-user")
    public ResponseEntity<InscriptionDTO> inscribeUserInFitnessCenter(@RequestBody InscriptionDTO inscriptionDTO) {
        InscriptionDTO inscriptionDTOResponse = inscriptionService.inscribeUserInFitnessCenter(inscriptionDTO);
        if (inscriptionDTOResponse == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(inscriptionDTOResponse);
    }

    @PostMapping("/accept-inscription/{inscriptionId}")
    public ResponseEntity<InscriptionDTO> acceptUserInscription(@PathVariable("inscriptionId") int inscriptionId) {
        Inscription inscription = inscriptionService.changeUserInscription(inscriptionId, InscriptionStatusEnum.ACCEPTED);
        if (inscription == null) {
            return ResponseEntity.noContent().build();
        }
        InscriptionDTO inscriptionDTO = new InscriptionDTO();
        inscriptionDTO.setId(inscription.getId());
        inscriptionDTO.setInscriptionStatus(inscription.getInscriptionStatus());
        return ResponseEntity.ok(inscriptionDTO);
    }

    @PostMapping("/reject-inscription/{inscriptionId}")
    public ResponseEntity<InscriptionDTO> rejectUserInscription(@PathVariable("inscriptionId") int inscriptionId) {
        Inscription inscription = inscriptionService.changeUserInscription(inscriptionId, InscriptionStatusEnum.REJECTED);
        if (inscription == null) {
            return ResponseEntity.noContent().build();
        }
        InscriptionDTO inscriptionDTO = new InscriptionDTO();
        inscriptionDTO.setId(inscription.getId());
        inscriptionDTO.setInscriptionStatus(inscription.getInscriptionStatus());
        return ResponseEntity.ok(inscriptionDTO);
    }

    @PostMapping("/inactive-inscription/{inscriptionId}")
    public ResponseEntity<InscriptionDTO> inactiveUserInscription(@PathVariable("inscriptionId") int inscriptionId) {
        Inscription inscription = inscriptionService.changeUserInscription(inscriptionId, InscriptionStatusEnum.INACTIVE);
        if (inscription == null) {
            return ResponseEntity.noContent().build();
        }
        InscriptionDTO inscriptionDTO = new InscriptionDTO();
        inscriptionDTO.setId(inscription.getId());
        inscriptionDTO.setInscriptionStatus(inscription.getInscriptionStatus());
        return ResponseEntity.ok(inscriptionDTO);
    }

    @GetMapping("/all/{email}")
    public ResponseEntity<List<InscriptionDTO>> getAllUserInscriptions(@PathVariable("email") String email) {
        List<InscriptionDTO> inscriptions = inscriptionService.getAllUserInscriptions(email);
        if (inscriptions == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(inscriptions);
    }
}
