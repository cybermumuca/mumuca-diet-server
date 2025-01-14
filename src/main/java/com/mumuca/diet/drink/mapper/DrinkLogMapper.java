package com.mumuca.diet.drink.mapper;

import com.mumuca.diet.drink.dto.CreateDrinkLogDTO;
import com.mumuca.diet.drink.dto.DrinkLogDTO;
import com.mumuca.diet.drink.dto.UpdateDrinkLogDTO;
import com.mumuca.diet.drink.model.DrinkLog;
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
