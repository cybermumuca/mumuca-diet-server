package com.mumuca.diet.config;

import com.mumuca.diet.hint.AppHintsRegistrar;
import com.mumuca.diet.hint.SecurityHintsRegistrar;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;

@Configuration
@ImportRuntimeHints({AppHintsRegistrar.class, SecurityHintsRegistrar.class})
public class NativeConfig {}

