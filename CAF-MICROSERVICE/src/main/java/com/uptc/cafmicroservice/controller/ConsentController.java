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
    // Inyección de la dependencia de ConsentService
    @Autowired
    ConsentService consentService;

    /**
     * Endpoint para subir archivos de inscripción.
     * @param inscriptionId El ID de la inscripción.
     * @param file El archivo de la inscripción que se va a cargar.
     * @param type El tipo de archivo de consentimiento.
     * @return Una ResponseEntity que indica si la carga fue exitosa.
     */
    @PostMapping("/upload/{inscriptionId}")
    public ResponseEntity<Boolean> uploadInscriptionFiles(@PathVariable int inscriptionId,
                                                          @RequestParam("inscriptionFile") MultipartFile file,
                                                          @RequestParam("fileType") ConsentTypeEnum type) {
        // Llama al método del servicio para guardar los archivos de consentimiento
        boolean wasCorrectlyUploaded = consentService.saveConsentFiles(inscriptionId, file, type);

        // Verifica si la carga fue exitosa y responde en consecuencia
        if (!wasCorrectlyUploaded) {
            return ResponseEntity.noContent().build(); // Devuelve un 204 No Content si falla la carga
        }
        return ResponseEntity.ok(true); // Devuelve un 200 OK con el valor true si la carga es exitosa
    }

    /**
     * Endpoint para cargar un archivo de inscripción.
     * @param inscriptionId El ID de la inscripción.
     * @param consentType El tipo de consentimiento solicitado.
     * @return Una ResponseEntity con el recurso del archivo o sin contenido si no se encuentra.
     */
    @GetMapping("/load")
    public ResponseEntity<Resource> loadInscriptionFile(@RequestParam("inscriptionId") int inscriptionId,
                                                        @RequestParam("consentType") ConsentTypeEnum consentType) {
        // Llama al método del servicio para cargar el archivo
        Resource resource = consentService.loadResource(inscriptionId, consentType);

        // Verifica si el recurso fue encontrado y responde en consecuencia
        if (resource == null) {
            return ResponseEntity.noContent().build(); // Devuelve un 204 No Content si el recurso no existe
        }
        return ResponseEntity.ok(resource); // Devuelve un 200 OK con el recurso si se encuentra
    }


}
