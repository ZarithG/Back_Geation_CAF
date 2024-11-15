package com.uptc.cafmicroservice.controller;

import com.uptc.cafmicroservice.dto.InscriptionDTO;
import com.uptc.cafmicroservice.entity.Inscription;
import com.uptc.cafmicroservice.enums.ConsentTypeEnum;
import com.uptc.cafmicroservice.enums.InscriptionStatusEnum;
import com.uptc.cafmicroservice.service.ConsentService;
import com.uptc.cafmicroservice.service.InscriptionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/caf/files")
public class ConsentController {
    @Autowired
    ConsentService consentService;

    @PostMapping("/upload/{inscriptionId}")
    public ResponseEntity<Boolean> uploadInscriptionFiles(@PathVariable int inscriptionId, @RequestParam("inscriptionFiles") MultipartFile[] files, @RequestParam("fileTypes") ConsentTypeEnum[] types){
        boolean wasCorrectlyUploaded = consentService.saveConsentFiles(inscriptionId, files, types);
        if (!wasCorrectlyUploaded) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(true);
    }

    @GetMapping("/load")
    public ResponseEntity<Resource> loadInscriptionFile(@RequestParam("inscriptionId") int inscriptionId, @RequestParam("consentType") ConsentTypeEnum consentType){
        Resource resource = consentService.loadResource(inscriptionId, consentType);
        if(resource == null){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(resource);
    }

}
