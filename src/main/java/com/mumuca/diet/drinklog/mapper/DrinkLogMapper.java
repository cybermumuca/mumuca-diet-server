package com.mumuca.diet.drinklog.mapper;

import com.mumuca.diet.dto.drinklog.CreateDrinkLogDTO;
import com.mumuca.diet.dto.drinklog.DrinkLogDTO;
import com.mumuca.diet.dto.drinklog.UpdateDrinkLogDTO;
import com.mumuca.diet.model.DrinkLog;
import com.mumuca.diet.model.User;
import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface DrinkLogMapper {
    DrinkLogDTO fromDrinkLogToDrinkLogDTO(DrinkLog drinkLog);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    DrinkLog fromCreateDrinkLogDTOToDrinkLog(CreateDrinkLogDTO dto, User user);
    DrinkLog fromDrinkLogDTOToDrinkLog(DrinkLogDTO drinkLogDTO);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateDrinkLogFromDTO(UpdateDrinkLogDTO dto, @MappingTarget DrinkLog drinkLog);
}
