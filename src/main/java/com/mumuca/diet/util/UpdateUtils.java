package com.mumuca.diet.util;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class UpdateUtils {

    /**
     * Atualiza um valor no objeto se ele for diferente do valor atual.
     *
     * @param getter   Função que obtém o valor atual do objeto.
     * @param setter   Função que define o novo valor no objeto.
     * @param newValue Novo valor que será comparado com o atual.
     * @param <T>      Tipo do valor que será comparado e atualizado.
     * @return {@code true} se o valor foi atualizado, {@code false} caso contrário.
     */
    public static <T> boolean updateIfDifferent(Supplier<T> getter, Consumer<T> setter, T newValue) {
        if (newValue != null && !newValue.equals(getter.get())) {
            setter.accept(newValue);
            return true;
        }

        return false;
    }
}
