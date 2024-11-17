package com.uptc.usersmicroservice.dto;

import com.uptc.usersmicroservice.entity.City;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DepartmentDTO {
    private int id;
    private String name;
    private List<CityDTO> cities;
}
