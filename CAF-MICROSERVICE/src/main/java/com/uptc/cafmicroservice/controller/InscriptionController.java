package com.uptc.cafmicroservice.controller;

import com.uptc.cafmicroservice.dto.InscriptionDTO;
import com.uptc.cafmicroservice.dto.UserInscriptionDTO;
import com.uptc.cafmicroservice.dto.UserResponseDTO;
import com.uptc.cafmicroservice.entity.Inscription;
import com.uptc.cafmicroservice.enums.ConsentTypeEnum;
import com.uptc.cafmicroservice.enums.InscriptionStatusEnum;
import com.uptc.cafmicroservice.service.InscriptionService;
import com.uptc.cafmicroservice.service.UserResponsesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/caf/inscription")
public class InscriptionController {
    // Inyección de la dependencia de InscriptionService
    @Autowired
    private InscriptionService inscriptionService;

    @Autowired
    private UserResponsesService userResponsesService;

    /**
     * Endpoint para inscribir a un usuario en un centro de fitness.
     * @param inscriptionDTO El DTO que contiene la información de la inscripción.
     * @param email El email del usuario a inscribir.
     * @return Una ResponseEntity con el InscriptionDTO de la inscripción realizada o sin contenido si falla.
     */
    @PostMapping("/inscribe-user/{email}")
    public ResponseEntity<InscriptionDTO> inscribeUserInFitnessCenter(@RequestBody InscriptionDTO inscriptionDTO, @PathVariable String email) {
        // Llama al método del servicio para inscribir al usuario
        InscriptionDTO inscriptionDTOResponse = inscriptionService.inscribeUserInFitnessCenter(inscriptionDTO, email);

        // Verifica si la inscripción fue exitosa y responde en consecuencia
        if (inscriptionDTOResponse == null) {
            return ResponseEntity.noContent().build(); // Devuelve un 204 No Content si la inscripción falla
        }
        return ResponseEntity.ok(inscriptionDTOResponse); // Devuelve un 200 OK con el DTO de la inscripción
    }

    /**
     * Endpoint para aceptar una inscripción de usuario.
     * @param inscriptionId El ID de la inscripción a aceptar.
     * @return Una ResponseEntity con el InscriptionDTO actualizado o sin contenido si falla.
     */
    @PostMapping("/accept-inscription/{inscriptionId}")
    public ResponseEntity<InscriptionDTO> acceptUserInscription(@PathVariable("inscriptionId") int inscriptionId) {
        // Llama al método del servicio para cambiar el estado de la inscripción a "ACCEPTED"
        Inscription inscription = inscriptionService.changeUserInscription(inscriptionId, InscriptionStatusEnum.ACCEPTED);

        // Verifica si el cambio fue exitoso y responde en consecuencia
        if (inscription == null) {
            return ResponseEntity.noContent().build(); // Devuelve un 204 No Content si el cambio falla
        }

        // Mapea el objeto Inscription a InscriptionDTO y devuelve la respuesta
        InscriptionDTO inscriptionDTO = new InscriptionDTO();
        inscriptionDTO.setId(inscription.getId());
        inscriptionDTO.setInscriptionStatus(inscription.getInscriptionStatus());
        return ResponseEntity.ok(inscriptionDTO);
    }

    /**
     * Endpoint para rechazar una inscripción de usuario.
     * @param inscriptionId El ID de la inscripción a rechazar.
     * @return Una ResponseEntity con el InscriptionDTO actualizado o sin contenido si falla.
     */
    @PostMapping("/reject-inscription/{inscriptionId}")
    public ResponseEntity<InscriptionDTO> rejectUserInscription(@PathVariable("inscriptionId") int inscriptionId) {
        // Llama al método del servicio para cambiar el estado de la inscripción a "REJECTED"
        Inscription inscription = inscriptionService.changeUserInscription(inscriptionId, InscriptionStatusEnum.REJECTED);

        // Verifica si el cambio fue exitoso y responde en consecuencia
        if (inscription == null) {
            return ResponseEntity.noContent().build(); // Devuelve un 204 No Content si el cambio falla
        }

        // Mapea el objeto Inscription a InscriptionDTO y devuelve la respuesta
        InscriptionDTO inscriptionDTO = new InscriptionDTO();
        inscriptionDTO.setId(inscription.getId());
        inscriptionDTO.setInscriptionStatus(inscription.getInscriptionStatus());
        return ResponseEntity.ok(inscriptionDTO);
    }

    /**
     * Endpoint para marcar una inscripción como inactiva.
     * @param inscriptionId El ID de la inscripción a marcar como inactiva.
     * @return Una ResponseEntity con el InscriptionDTO actualizado o sin contenido si falla.
     */
    @PostMapping("/inactive-inscription/{inscriptionId}")
    public ResponseEntity<InscriptionDTO> inactiveUserInscription(@PathVariable("inscriptionId") int inscriptionId) {
        // Llama al método del servicio para cambiar el estado de la inscripción a "INACTIVE"
        Inscription inscription = inscriptionService.changeUserInscription(inscriptionId, InscriptionStatusEnum.INACTIVE);

        // Verifica si el cambio fue exitoso y responde en consecuencia
        if (inscription == null) {
            return ResponseEntity.noContent().build(); // Devuelve un 204 No Content si el cambio falla
        }

        // Mapea el objeto Inscription a InscriptionDTO y devuelve la respuesta
        InscriptionDTO inscriptionDTO = new InscriptionDTO();
        inscriptionDTO.setId(inscription.getId());
        inscriptionDTO.setInscriptionStatus(inscription.getInscriptionStatus());
        return ResponseEntity.ok(inscriptionDTO);
    }

    /**
     * Endpoint para marcar una inscripción como activa.
     * @param inscriptionId El ID de la inscripción a marcar como inactiva.
     * @return Una ResponseEntity con el InscriptionDTO actualizado o sin contenido si falla.
     */
    @PostMapping("/active-inscription/{inscriptionId}")
    public ResponseEntity<InscriptionDTO> activeUserInscription(@PathVariable("inscriptionId") int inscriptionId) {
        // Llama al método del servicio para cambiar el estado de la inscripción a "ACCEPTED"
        Inscription inscription = inscriptionService.changeUserInscription(inscriptionId, InscriptionStatusEnum.ACCEPTED);

        // Verifica si el cambio fue exitoso y responde en consecuencia
        if (inscription == null) {
            return ResponseEntity.noContent().build(); // Devuelve un 204 No Content si el cambio falla
        }

        // Mapea el objeto Inscription a InscriptionDTO y devuelve la respuesta
        InscriptionDTO inscriptionDTO = new InscriptionDTO();
        inscriptionDTO.setId(inscription.getId());
        inscriptionDTO.setInscriptionStatus(inscription.getInscriptionStatus());
        return ResponseEntity.ok(inscriptionDTO);
    }

    @GetMapping("/all/{email}")
    public ResponseEntity<List<InscriptionDTO>> getAllUserInscriptions(@PathVariable("email") String email) {
        // Llama al método del servicio para obtener todas las inscripciones del usuario
        List<InscriptionDTO> inscriptions = inscriptionService.findAllUserInscriptions(email);

        // Verifica si la lista es nula y responde en consecuencia
        if (inscriptions == null) {
            return ResponseEntity.noContent().build(); // Devuelve un 204 No Content si no hay inscripciones
        }
        return ResponseEntity.ok(inscriptions); // Devuelve un 200 OK con la lista de inscripciones
    }

    /**
     * Endpoint para obtener todas las inscripciones de un usuario.
     * @param email El email del usuario cuyas inscripciones se van a obtener.
     * @return Una ResponseEntity con la lista de InscriptionDTO o sin contenido si no se encuentran inscripciones.
     */
    @GetMapping("/all-active/{email}")
    public ResponseEntity<List<InscriptionDTO>> getAllUserActiveInscriptions(@PathVariable("email") String email) {
        // Llama al método del servicio para obtener todas las inscripciones del usuario
        List<InscriptionDTO> inscriptions = inscriptionService.getAllUserActiveInscriptions(email);

        // Verifica si la lista es nula y responde en consecuencia
        if (inscriptions == null) {
            return ResponseEntity.noContent().build(); // Devuelve un 204 No Content si no hay inscripciones
        }
        return ResponseEntity.ok(inscriptions); // Devuelve un 200 OK con la lista de inscripciones
    }

    @GetMapping("/all/coordinator-email/{email}")
    public ResponseEntity<List<UserInscriptionDTO>> getAllUserInscriptionsToCaf(@PathVariable("email") String email) {
        // Llama al método del servicio para obtener todas las inscripciones del usuario
        List<UserInscriptionDTO> inscriptions = inscriptionService.getAllInscriptionByFitnessCenter(email);

        // Verifica si la lista es nula y responde en consecuencia
        if (inscriptions == null) {
            return ResponseEntity.noContent().build(); // Devuelve un 204 No Content si no hay inscripciones
        }
        return ResponseEntity.ok(inscriptions); // Devuelve un 200 OK con la lista de inscripciones
    }

    @GetMapping("/all-active/coordinator-email/{email}")
    public ResponseEntity<List<UserInscriptionDTO>> getAllUserActiveInscriptionsToCaf(@PathVariable("email") String email) {
        // Llama al método del servicio para obtener todas las inscripciones del usuario
        List<UserInscriptionDTO> inscriptions = inscriptionService.getAllActiveInscriptionByFitnessCenter(email);

        // Verifica si la lista es nula y responde en consecuencia
        if (inscriptions == null) {
            return ResponseEntity.noContent().build(); // Devuelve un 204 No Content si no hay inscripciones
        }
        return ResponseEntity.ok(inscriptions); // Devuelve un 200 OK con la lista de inscripciones
    }

    @GetMapping("/user-responses/{inscriptionId}")
    public ResponseEntity<List<UserResponseDTO>> getUserResponse(@PathVariable("inscriptionId") int inscriptionId) {
        List<UserResponseDTO> userResponseDTOList = userResponsesService.getUserResponsesByInscriptionId(inscriptionId);
        if (userResponseDTOList == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(userResponseDTOList);
    }
}
