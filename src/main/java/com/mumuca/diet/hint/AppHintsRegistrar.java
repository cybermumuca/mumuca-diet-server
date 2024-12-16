package com.mumuca.diet.hint;

import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppHintsRegistrar implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        hints.resources().registerPattern("application*.yml");
        hints.resources().registerPattern("logback-spring.xml");
        hints.resources().registerPattern("app*");
        hints.resources().registerPattern("db/migration/**");
    }
}