package com.uptc.cafmicroservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserBasicDTO {
    private int id;
    private String email;
}
