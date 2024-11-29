package com.uptc.cafmicroservice.dto;

import com.uptc.cafmicroservice.entity.Inscription;
import com.uptc.cafmicroservice.entity.Sectional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FitnessCenterDTO {
    private int id;
    private String name;
    private String description;
    private Sectional sectional;
    private String coordinatorEmail;
    private String coordinatorName;
    private Set<Inscription> inscriptions = new HashSet<>();
}
