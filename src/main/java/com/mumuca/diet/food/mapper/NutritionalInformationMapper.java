package com.mumuca.diet.food.mapper;

import com.mumuca.diet.dto.food.CreateNutritionalInformationDTO;
import com.mumuca.diet.dto.food.NutritionalInformationDTO;
import com.mumuca.diet.model.NutritionalInformation;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NutritionalInformationMapper {
    NutritionalInformation fromCreateNIDTOToNI(CreateNutritionalInformationDTO createNutritionalInformationDTO);
    NutritionalInformationDTO fromNIToNIDTO(NutritionalInformation nutritionalInformation);
}
