package com.mumuca.diet.util;

import com.mumuca.diet.dto.MacronutrientDTO;
import com.mumuca.diet.model.ActivityLevel;
import com.mumuca.diet.model.Gender;
import com.mumuca.diet.model.GoalType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

public class NutritionalCalculator {
    /**
     * Calcula o IMC (Índice de Massa Corporal).
     * Fórmula: peso / (altura²)
     *
     * @param weight Peso em kg.
     * @param height Altura em metros.
     * @return O valor do IMC arredondado para 2 casas decimais.
     */
    public static BigDecimal calculateBMI(BigDecimal weight, BigDecimal height) {
        if (weight == null || height == null || height.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("Weight and height must be non-null and height > 0.");
        }

        return weight.divide(height.pow(2), 2, RoundingMode.HALF_UP);
    }

    /**
     * Calcula o BMR (Taxa Metabólica Basal) usando a fórmula revisada de Harris-Benedict.
     * <p>
     * Fórmula para homens: 88.362 + (13.397 * peso em kg) + (4.799 * altura em cm) - (5.677 * idade)
     * <p>
     * Fórmula para mulheres: 447.593 + (9.247 * peso em kg) + (3.098 * altura em cm) - (4.330 * idade)
     * <p>
     * Fonte: <a href="https://ge.globo.com/eu-atleta/nutricao/noticia/2023/04/18/c-tmb-o-que-e-metabolismo-basal-como-calcular-e-usar-para-emagrecer.ghtml">Globo Nutrição</a>
     *
     * @param weight Peso em kg.
     * @param height Altura em metros.
     * @param age Idade em anos.
     * @param gender Gênero do usuário (MALE ou FEMALE).
     * @return O valor do BMR arredondado para 2 casas decimais.
     */
    public static BigDecimal calculateBMR(BigDecimal weight, BigDecimal height, int age, Gender gender) {
        if (weight == null || height == null || age <= 0 || gender == null) {
            throw new IllegalArgumentException("Weight, height, age, and gender must be valid.");
        }

        BigDecimal heightInCm = height.multiply(BigDecimal.valueOf(100)); // Convertendo altura para cm

        if (gender == Gender.MALE) {
            return BigDecimal.valueOf(88.362)
                    .add(BigDecimal.valueOf(13.397).multiply(weight))
                    .add(BigDecimal.valueOf(4.799).multiply(heightInCm))
                    .subtract(BigDecimal.valueOf(5.677).multiply(BigDecimal.valueOf(age)))
                    .setScale(0, RoundingMode.HALF_UP);
        } else {
            return BigDecimal.valueOf(447.593)
                    .add(BigDecimal.valueOf(9.247).multiply(weight))
                    .add(BigDecimal.valueOf(3.098).multiply(heightInCm))
                    .subtract(BigDecimal.valueOf(4.330).multiply(BigDecimal.valueOf(age)))
                    .setScale(0, RoundingMode.HALF_UP);
        }
    }

    /**
     * Ajusta o BMR com base no nível de atividade física.
     *
     * @param bmr Taxa metabólica basal.
     * @param activityLevel Nível de atividade física (SEDENTARY, LIGHTLY_ACTIVE, MODERATELY_ACTIVE, VERY_ACTIVE, EXTRA_ACTIVE).
     * @return Calorias ajustadas com base na atividade.
     */
    public static BigDecimal adjustCaloriesForActivity(BigDecimal bmr, ActivityLevel activityLevel) {
        if (bmr == null || activityLevel == null) {
            throw new IllegalArgumentException("BMR and activity level must be valid.");
        }

        BigDecimal activityFactor = switch (activityLevel) {
            case SEDENTARY -> BigDecimal.valueOf(1.2);
            case LIGHTLY_ACTIVE -> BigDecimal.valueOf(1.375);
            case MODERATELY_ACTIVE -> BigDecimal.valueOf(1.55);
            case VERY_ACTIVE -> BigDecimal.valueOf(1.725);
            case EXTRA_ACTIVE -> BigDecimal.valueOf(1.9);
        };

        return bmr.multiply(activityFactor).setScale(0, RoundingMode.HALF_UP);
    }


    /**
     * Calcula a meta diária de ingestão de água com base no peso.
     * Fórmula padrão: 35 ml por kg de peso.
     *
     * @param weight Peso do usuário em kg.
     * @return Meta de ingestão de água em litros.
     */
    public static BigDecimal calculateWaterIntake(BigDecimal weight) {
        if (weight == null || weight.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Weight must be valid.");
        }

        BigDecimal waterMl = weight.multiply(BigDecimal.valueOf(35));
        return waterMl.divide(BigDecimal.valueOf(1000), 2, RoundingMode.HALF_UP); // ml -> L
    }

    /**
     * Ajusta as calorias com base no objetivo do usuário (ganhar, perder ou manter peso).
     *
     * @param calories Necessidade calórica ajustada.
     * @param goalType Objetivo do usuário (LOSE_WEIGHT, MAINTAIN_WEIGHT, GAIN_WEIGHT).
     * @return Calorias ajustadas para o objetivo.
     */
    public static BigDecimal adjustCaloriesForGoal(BigDecimal calories, GoalType goalType) {
        if (calories == null || goalType == null) {
            throw new IllegalArgumentException("Calories and goal type must be valid.");
        }

        return switch (goalType) {
            case LOSE_WEIGHT -> calories.multiply(BigDecimal.valueOf(0.8)).setScale(0, RoundingMode.HALF_UP);
            case GAIN_WEIGHT -> calories.multiply(BigDecimal.valueOf(1.2)).setScale(0, RoundingMode.HALF_UP);
            case MAINTAIN_WEIGHT -> calories.setScale(0, RoundingMode.HALF_UP);
        };
    }

    /**
     * Calcula a meta de macronutrientes (proteínas, carboidratos, gorduras) com base nas calorias.
     * A distribuição pode ser ajustada conforme necessidade.
     *
     * @param targetCalories Meta calórica diária.
     * @param proteinPercentage Percentual de calorias destinadas às proteínas (em decimal).
     * @param carbsPercentage Percentual de calorias destinadas aos carboidratos (em decimal).
     * @param fatPercentage Percentual de calorias destinadas às gorduras (em decimal).
     * @return Um objeto MacronutrientDTO com as metas de proteínas, carboidratos e gorduras.
     */
    public static MacronutrientDTO calculateMacronutrients(
            BigDecimal targetCalories,
            BigDecimal proteinPercentage,
            BigDecimal carbsPercentage,
            BigDecimal fatPercentage
    ) {
        if (targetCalories == null || targetCalories.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Target calories must be valid.");
        }

        BigDecimal totalPercentage = proteinPercentage.add(carbsPercentage).add(fatPercentage);
        if (totalPercentage.compareTo(BigDecimal.ONE) != 0) {
            throw new IllegalArgumentException("The sum of protein, carbs, and fat percentages must equal 1 (100%).");
        }

        BigDecimal proteinCalories = targetCalories.multiply(proteinPercentage);
        BigDecimal carbsCalories = targetCalories.multiply(carbsPercentage);
        BigDecimal fatCalories = targetCalories.multiply(fatPercentage);

        // Cálculo de gramas com base nas calorias
        return MacronutrientDTO.builder()
                .protein(proteinCalories.divide(BigDecimal.valueOf(4), 2, RoundingMode.HALF_UP).floatValue()) // 1g proteína = 4 calorias
                .carbs(carbsCalories.divide(BigDecimal.valueOf(4), 2, RoundingMode.HALF_UP).floatValue()) // 1g carboidrato = 4 calorias
                .fat(fatCalories.divide(BigDecimal.valueOf(9), 2, RoundingMode.HALF_UP).floatValue()) // 1g gordura = 9 calorias
                .build();
    }

    /**
     * Classifica o IMC com base nos padrões da OMS.
     *
     * @param bmi Valor do IMC.
     * @return Classificação do IMC (ex: "Normal", "Sobrepeso", "Obesidade Grau 1").
     */
    public static String classifyBMI(BigDecimal bmi) {
        if (bmi == null || bmi.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("BMI must be valid.");
        }

        if (bmi.compareTo(BigDecimal.valueOf(18.5)) < 0) return "Underweight";
        if (bmi.compareTo(BigDecimal.valueOf(24.9)) <= 0) return "Healthy";
        if (bmi.compareTo(BigDecimal.valueOf(29.9)) <= 0) return "Overweight";
        if (bmi.compareTo(BigDecimal.valueOf(34.9)) <= 0) return "Obesity I";
        if (bmi.compareTo(BigDecimal.valueOf(39.9)) <= 0) return "Obesity II";

        return "Obesity III";
    }


    /**
     * Calcula a data estimada para atingir o objetivo de peso do usuário com base no tipo de meta,
     * no peso atual e no peso alvo.
     * <p>
     * A lógica de cálculo é a seguinte:
     * <p>
     * - Para perda de peso, assume-se uma perda de 0.75 kg por semana.
     * <p>
     * - Para ganho de peso, assume-se um ganho de 0.375 kg por semana.
     * <p>
     * - Para manutenção de peso, é atribuído um prazo fixo de 26 semanas (aproximadamente 6 meses).
     * <p>
     * O cálculo considera o número de semanas necessárias para atingir a meta e adiciona isso à data atual,
     * retornando a data estimada de conclusão.
     *
     * @param goalType Tipo de objetivo do usuário (pode ser LOSE_WEIGHT, GAIN_WEIGHT, ou MAINTAIN_WEIGHT).
     * @param currentWeight Peso(kg) atual do usuário.
     * @param targetWeight Peso(kg) alvo do usuário.
     * @return A data estimada de conclusão da meta, considerando o número de semanas necessário para atingir o objetivo.
     */
    public static LocalDate calculateDeadline(GoalType goalType, BigDecimal currentWeight, BigDecimal targetWeight) {
        long weeksToAchieveGoal;

        switch (goalType) {
            case LOSE_WEIGHT -> {
                // Perda de peso: 0.75 kg/semana
                BigDecimal weightToLose = currentWeight.subtract(targetWeight);
                weeksToAchieveGoal = weightToLose.divide(BigDecimal.valueOf(0.75), RoundingMode.CEILING).longValue();
            }
            case GAIN_WEIGHT -> {
                // Ganho de peso: 0.375 kg/semana
                BigDecimal weightToGain = targetWeight.subtract(currentWeight);
                weeksToAchieveGoal = weightToGain.divide(BigDecimal.valueOf(0.375), RoundingMode.CEILING).longValue();
            }
            default -> {
                // Manutenção de peso: Prazo padrão (ex: 6 meses)
                weeksToAchieveGoal = 26;
            }
        }

        // Calcula a data final com base nas semanas
        return LocalDate.now().plusWeeks(weeksToAchieveGoal);
    }

    /**
     * Calcula o peso ideal mínimo e máximo com base no IMC (Índice de Massa Corporal)
     * para uma pessoa de determinada altura.
     *
     * @param height A altura em metros.
     * @return Um array contendo o peso mínimo e o peso máximo ideal para a altura fornecida.
     */
    public static List<BigDecimal> calculateIdealWeight(BigDecimal height) {
        BigDecimal minWeight = BigDecimal.valueOf(18.5).multiply(height.pow(2)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal maxWeight = BigDecimal.valueOf(24.9).multiply(height.pow(2)).setScale(2, RoundingMode.HALF_UP);

        return List.of(minWeight, maxWeight);
    }

    /**
     * Estima o percentual de gordura corporal com base no IMC e idade.
     * A fórmula utilizada varia para homens e mulheres.
     *
     * @param weight Peso em kg.
     * @param height Altura em metros.
     * @param age Idade em anos.
     * @param gender Gênero da pessoa (MALE, FEMALE).
     * @return Estimativa do percentual de gordura corporal.
     */
    public static BigDecimal calculateBodyFat(BigDecimal weight, BigDecimal height, int age, Gender gender) {
        BigDecimal bmi = calculateBMI(weight, height);

        BigDecimal genderConstant = gender == Gender.MALE ? BigDecimal.valueOf(16.2) : BigDecimal.valueOf(5.4);

        return bmi.multiply(BigDecimal.valueOf(1.20))
                .add(BigDecimal.valueOf(0.23).multiply(BigDecimal.valueOf(age)))
                .subtract(genderConstant)
                .setScale(2, RoundingMode.HALF_UP);
    }
}
