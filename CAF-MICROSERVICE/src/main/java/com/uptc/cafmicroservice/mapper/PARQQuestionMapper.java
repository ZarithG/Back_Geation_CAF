package com.uptc.cafmicroservice.mapper;

import com.uptc.cafmicroservice.dto.PARQQuestionDTO;
import com.uptc.cafmicroservice.entity.PARQQuestion;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PARQQuestionMapper {
    PARQQuestionMapper INSTANCE = Mappers.getMapper(PARQQuestionMapper.class);

    PARQQuestion mapPARQQuestionDTOToPARQQuestion(PARQQuestionDTO parqQuestionDTO);
    PARQQuestionDTO mapPARQQuestionToPARQQuestionDTO(PARQQuestion parqQuestion);
}
