package com.mumuca.diet.model;

public enum Unit {
    GRAM("g"),
    MILLIGRAM("mg"),
    KILOGRAM("kg"),
    MICROGRAM("mcg"),
    MILLILITER("ml"),
    LITER("l"),
    CALORIE("kcal"),
    KILOJOULE("kj"),
    INTERNATIONAL_UNIT("iu"),
    OUNCE("oz"),
    CUP("cup"),
    TABLESPOON("tbsp"),
    TEASPOON("tsp"),
    SLICE("slice"),
    PIECE("piece"),
    BOWL("bowl");

    private final String value;

    Unit(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Unit fromValue(String value) {
        for (Unit unit : Unit.values()) {
            if (unit.value.equals(value)) {
                return unit;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
