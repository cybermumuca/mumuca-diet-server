package com.mumuca.diet.meal.mapper;

import com.mumuca.diet.dto.meal.MealDTO;
import com.mumuca.diet.model.Meal;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MealMapper {
    MealDTO fromMealToMealDTO(Meal meal);
}
