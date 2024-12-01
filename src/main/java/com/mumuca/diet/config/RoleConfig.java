package com.mumuca.diet.config;

import com.mumuca.diet.model.Role;
import com.mumuca.diet.repository.RolesRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@AllArgsConstructor
public class RoleConfig implements CommandLineRunner {

    private final RolesRepository rolesRepository;

    @Override
    @Transactional
    public void run(String... args) {
        List<String> roleAuthorities = List.of("USER", "ADMIN");

        for (String roleAuthority : roleAuthorities) {
            rolesRepository.findByAuthority(roleAuthority)
                    .orElseGet(() -> rolesRepository.save(new Role(null, roleAuthority)));
        }
    }
}
