package com.mumuca.diet.food.mapper;

import com.mumuca.diet.food.dto.CreateNutritionalInformationDTO;
import com.mumuca.diet.food.dto.NutritionalInformationDTO;
import com.mumuca.diet.food.model.NutritionalInformation;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NutritionalInformationMapper {
    NutritionalInformation fromCreateNIDTOToNI(CreateNutritionalInformationDTO createNutritionalInformationDTO);
    NutritionalInformationDTO fromNIToNIDTO(NutritionalInformation nutritionalInformation);
}
