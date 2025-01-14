package com.mumuca.diet.food.mapper;

import com.mumuca.diet.food.dto.CreateNutritionalInformationDTO;
import com.mumuca.diet.food.dto.NutritionalInformationDTO;
import com.mumuca.diet.food.dto.UpdateNutritionalInformationDTO;
import com.mumuca.diet.food.model.NutritionalInformation;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NutritionalInformationMapper {
    NutritionalInformation fromCreateNIDTOToNI(CreateNutritionalInformationDTO createNutritionalInformationDTO);
    NutritionalInformationDTO fromNIToNIDTO(NutritionalInformation nutritionalInformation);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateNIFromUpdateNIDTO(UpdateNutritionalInformationDTO updateNIDTO, @MappingTarget NutritionalInformation ni);
}
