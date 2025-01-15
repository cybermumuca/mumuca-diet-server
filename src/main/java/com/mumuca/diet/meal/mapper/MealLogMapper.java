package com.mumuca.diet.meal.mapper;

import com.mumuca.diet.dto.meallog.MealLogDTO;
import com.mumuca.diet.model.MealLog;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MealLogMapper {
    MealLogDTO fromMealLogToMealLogDTO(MealLog mealLog);
}
