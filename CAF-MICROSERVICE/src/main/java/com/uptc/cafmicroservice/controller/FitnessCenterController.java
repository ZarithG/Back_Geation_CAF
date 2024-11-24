package com.uptc.cafmicroservice.controller;

import com.uptc.cafmicroservice.dto.FitnessCenterDTO;
import com.uptc.cafmicroservice.entity.FitnessCenter;
import com.uptc.cafmicroservice.mapper.FitnessCenterMapper;
import com.uptc.cafmicroservice.service.FitnessCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/caf")
public class FitnessCenterController {
    // Inyección de la dependencia de FitnessCenterService
    @Autowired
    FitnessCenterService fitnessCenterService;

    /**
     * Endpoint para obtener todos los centros de fitness.
     * @return Una ResponseEntity con la lista de FitnessCenterDTO o sin contenido si la lista está vacía.
     */
    @GetMapping("/all")
    public ResponseEntity<List<FitnessCenterDTO>> getAllFitnessCenter() {
        // Llama al método del servicio para obtener la lista de centros de fitness
        List<FitnessCenter> fitnessCenterList = fitnessCenterService.getAll();

        // Verifica si la lista está vacía y responde en consecuencia
        if (fitnessCenterList.isEmpty()) {
            return ResponseEntity.noContent().build(); // Devuelve un 204 No Content si no hay centros de fitness
        }

        // Devuelve un 200 OK con la lista de DTOs mapeada
        return ResponseEntity.ok(FitnessCenterMapper.INSTANCE.mapFitnessCenterListToFitnessCenterDTOList(fitnessCenterList));
    }

    /**
     * Endpoint para obtener un fitness center por su id
     * @param fitnessCenterId Id del fitness center
     * @return  Una ResponseEntity con el FitnessCenterDTO o sin contenido si no existe el FitnessCenter
     */
    @GetMapping("/id/{fitnessCenterId}")
    public ResponseEntity<FitnessCenterDTO> getFitnessCenterById(@PathVariable("fitnessCenterId") int fitnessCenterId) {
        FitnessCenterDTO fitnessCenterDTO = fitnessCenterService.obtainFitnessCenterById(fitnessCenterId);
        if(fitnessCenterDTO == null){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(fitnessCenterDTO);
    }

    /**
     * Endpoint para cambiar el coordinador de un centro de fitness.
     * @param fitnessCenterDTO El DTO que contiene los datos del centro de fitness y el nuevo email del coordinador.
     * @return Una ResponseEntity con el FitnessCenterDTO actualizado o sin contenido si no se puede realizar el cambio.
     */
    @PostMapping("/change-coordinator")
    public ResponseEntity<FitnessCenterDTO> changeFitnessCenterCoordinator(@RequestBody FitnessCenterDTO fitnessCenterDTO) {
        // Llama al método del servicio para cambiar el coordinador del centro de fitness
        FitnessCenter fitnessCenter = fitnessCenterService.changeFitnessCenterCoordinator(fitnessCenterDTO.getId(), fitnessCenterDTO.getCoordinatorEmail());

        // Verifica si el cambio fue exitoso y responde en consecuencia
        if (fitnessCenter == null) {
            return ResponseEntity.noContent().build(); // Devuelve un 204 No Content si no se pudo realizar el cambio
        }

        // Devuelve un 200 OK con el DTO del centro de fitness actualizado
        return ResponseEntity.ok(FitnessCenterMapper.INSTANCE.mapFitnessCenterToFitnessCenterDTO(fitnessCenter));
    }

    @GetMapping("/{coordinatorEmail}")
    public ResponseEntity<Integer> getFitnessCenterByCoordinatorEmail(@PathVariable String coordinatorEmail) {
        int fitnessCenterId = fitnessCenterService.obtainFitnessCenterIdByCoordinatorEmail(coordinatorEmail);
        if (fitnessCenterId == 0) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(fitnessCenterId);
    }

}
