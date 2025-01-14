package com.mumuca.diet.food.mapper;

import com.mumuca.diet.food.dto.CreatePortionDTO;
import com.mumuca.diet.food.dto.PortionDTO;
import com.mumuca.diet.food.model.Portion;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PortionMapper {
    PortionDTO fromPortionToPortionDTO(Portion portion);
    Portion fromPortionDTOToPortion(PortionDTO portionDTO);
    Portion fromCreatePortionDTOToPortion(CreatePortionDTO createPortionDTO);
}
