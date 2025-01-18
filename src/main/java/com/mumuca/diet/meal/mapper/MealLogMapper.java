package com.mumuca.diet.meal.mapper;

import com.mumuca.diet.meal.dto.MealLogDTO;
import com.mumuca.diet.meal.model.MealLog;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MealLogMapper {
    MealLogDTO fromMealLogToMealLogDTO(MealLog mealLog);
}
