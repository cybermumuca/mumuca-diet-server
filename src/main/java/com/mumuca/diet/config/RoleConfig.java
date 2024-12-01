package com.mumuca.diet.config;

import com.mumuca.diet.model.Role;
import com.mumuca.diet.repository.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@AllArgsConstructor
public class RoleConfig implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public void run(String... args) {
        List<String> roleAuthorities = List.of("USER", "ADMIN");

        for (String roleAuthority : roleAuthorities) {
            roleRepository.findByAuthority(roleAuthority)
                    .orElseGet(() -> roleRepository.save(new Role(null, roleAuthority)));
        }
    }
}
