package com.uptc.shiftmicroservice.mapper;

import com.uptc.shiftmicroservice.dto.ShiftDTO;
import com.uptc.shiftmicroservice.entity.Shift;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ShiftMapper {

    /**
     * Instancia única de `ShiftMapper`, gestionada por MapStruct.
     * Se utiliza para invocar las operaciones de mapeo.
     */
    ShiftMapper INSTANCE = Mappers.getMapper(ShiftMapper.class);

    /**
     * Convierte un objeto de tipo `Shift` a su representación de DTO (`ShiftDTO`).
     *
     * @param shift Objeto `Shift` que se desea convertir.
     * @return Objeto `ShiftDTO` con los datos mapeados desde el `Shift`.
     */
    @Mapping(source = "dayAssignment.id", target = "dayAssignment")
    ShiftDTO shiftToShiftDTO(Shift shift);

    /**
     * Convierte un objeto de tipo `ShiftDTO` a su representación de entidad (`Shift`).
     *
     * @param shiftDTO Objeto `ShiftDTO` que se desea convertir.
     * @return Objeto `Shift` con los datos mapeados desde el `ShiftDTO`.
     */
    @Mapping(source = "dayAssignment", target = "dayAssignment.id")
    Shift shiftDTOToShift(ShiftDTO shiftDTO);
}
