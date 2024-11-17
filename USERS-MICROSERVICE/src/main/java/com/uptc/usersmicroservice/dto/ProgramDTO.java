package com.uptc.usersmicroservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProgramDTO {
    private int id;
    private String programName;
    private FacultyDTO facultyDTO;
}
