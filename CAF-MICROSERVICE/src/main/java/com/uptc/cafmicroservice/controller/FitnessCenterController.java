package com.uptc.cafmicroservice.controller;

import com.uptc.cafmicroservice.dto.FitnessCenterDTO;
import com.uptc.cafmicroservice.entity.FitnessCenter;
import com.uptc.cafmicroservice.mapper.FitnessCenterMapper;
import com.uptc.cafmicroservice.service.FitnessCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/caf")
public class FitnessCenterController {
    @Autowired
    FitnessCenterService fitnessCenterService;

    @GetMapping("/all")
    public ResponseEntity<List<FitnessCenterDTO>> getAllFitnessCenter() {
        List<FitnessCenter> fitnessCenterList = fitnessCenterService.getAll();
        if (fitnessCenterList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(FitnessCenterMapper.INSTANCE.mapFitnessCenterListToFitnessCenterDTOList
                (fitnessCenterList));
    }

    @PostMapping("/change-coordinator")
    public ResponseEntity<FitnessCenterDTO> changeFitnessCenterCoordinator(@RequestBody FitnessCenterDTO fitnessCenterDTO){
        FitnessCenter fitnessCenter = fitnessCenterService.changeFitnessCenterCoordinator(fitnessCenterDTO.getId(), fitnessCenterDTO.getCoordinatorEmail());
        if (fitnessCenter == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(FitnessCenterMapper.INSTANCE.mapFitnessCenterToFitnessCenterDTO(fitnessCenter));
    }
}
