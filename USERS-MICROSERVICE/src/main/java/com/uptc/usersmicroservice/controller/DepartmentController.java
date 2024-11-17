package com.uptc.usersmicroservice.controller;

import com.uptc.usersmicroservice.dto.DepartmentDTO;
import com.uptc.usersmicroservice.entity.Department;
import com.uptc.usersmicroservice.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/user")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/departments")
    public ResponseEntity<List<DepartmentDTO>> getAllDepartments() {
        List<DepartmentDTO> departmentsDTO = departmentService.getAllDepartments();
        if (departmentsDTO.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(departmentsDTO);
    }
}

