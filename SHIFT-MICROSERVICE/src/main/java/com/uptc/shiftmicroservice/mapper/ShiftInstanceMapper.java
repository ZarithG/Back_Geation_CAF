package com.uptc.shiftmicroservice.mapper;

import com.uptc.shiftmicroservice.dto.ShiftInstanceDTO;
import com.uptc.shiftmicroservice.entity.ShiftInstance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ShiftInstanceMapper {

    /**
     * Instancia única de `ShiftInstanceMapper`, gestionada por MapStruct.
     * Esta instancia se utiliza para realizar las operaciones de mapeo.
     */
    ShiftInstanceMapper INSTANCE = Mappers.getMapper(ShiftInstanceMapper.class);

    /**
     * Convierte un objeto de tipo `ShiftInstance` a su representación de DTO (`ShiftInstanceDTO`).
     *
     * @param shift Objeto `ShiftInstance` que se desea convertir.
     * @return Objeto `ShiftInstanceDTO` con los datos mapeados desde el `ShiftInstance`.
     */
    @Mapping(source = "dayAssignment.id", target = "dayAssignment")
    ShiftInstanceDTO shiftInstanceToShiftInstanceDTO(ShiftInstance shift);

    /**
     * Convierte un objeto de tipo `ShiftInstanceDTO` a su representación de entidad (`ShiftInstance`).
     *
     * @param shiftDTO Objeto `ShiftInstanceDTO` que se desea convertir.
     * @return Objeto `ShiftInstance` con los datos mapeados desde el `ShiftInstanceDTO`.
     */
    @Mapping(source = "dayAssignment", target = "dayAssignment.id")
    ShiftInstance shiftInstanceDTOToShiftInstance(ShiftInstanceDTO shiftDTO);
}
