package com.mumuca.diet.meal.model;

import lombok.Getter;

@Getter
public enum MealType {
    BREAKFAST("Breakfast"),       // Café da manhã
    BRUNCH("Brunch"),             // Café da manhã tardio
    LUNCH("Lunch"),               // Almoço
    AFTERNOON_SNACK("Afternoon Snack"), // Lanche da tarde
    DINNER("Dinner"),             // Jantar
    SUPPER("Supper"),             // Ceia
    SNACK("Snack"),               // Lanches rápidos
    PRE_WORKOUT("Pre-Workout"),   // Antes do treino
    POST_WORKOUT("Post-Workout"), // Após o treino
    MIDNIGHT_SNACK("Midnight Snack"); // Lanche da madrugada

    private final String displayName;

    MealType(String displayName) {
        this.displayName = displayName;
    }

}