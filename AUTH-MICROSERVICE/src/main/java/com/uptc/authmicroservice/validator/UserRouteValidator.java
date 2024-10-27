package com.uptc.authmicroservice.validator;

import com.uptc.authmicroservice.dto.RequestDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Component
@ConfigurationProperties(prefix = "user-paths")

public class UserRouteValidator {
    private List<RequestDTO> paths;

    public boolean isUserPath(RequestDTO requestDTO){
        return paths.stream().anyMatch(p->
                Pattern.matches(p.getUri(), requestDTO.getUri()) && p.getMethod().equals(requestDTO.getMethod()));
    }
}
