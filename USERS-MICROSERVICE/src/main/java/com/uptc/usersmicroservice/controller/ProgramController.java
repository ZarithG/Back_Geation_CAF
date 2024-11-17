package com.uptc.usersmicroservice.controller;

import com.uptc.usersmicroservice.dto.ProgramDTO;
import com.uptc.usersmicroservice.service.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/user")
public class ProgramController {
    @Autowired
    private ProgramService programService;

    @GetMapping("/programs")
    public ResponseEntity<List<ProgramDTO>> getAllDepartments() {
        List<ProgramDTO> programsDTOList = programService.getAllPrograms();
        if (programsDTOList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(programsDTOList);
    }
}
