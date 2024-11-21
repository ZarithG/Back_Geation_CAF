package com.uptc.shiftmicroservice.dto;
import lombok.*;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ShiftInstanceDTO {

    private long id;

    private boolean state;

    private long shift;

    private LocalDate date;

    private int placeAvailable;
}
