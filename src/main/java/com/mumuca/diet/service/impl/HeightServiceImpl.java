package com.mumuca.diet.service.impl;

import com.mumuca.diet.dto.HeightDTO;
import com.mumuca.diet.dto.HeightRegistryDTO;
import com.mumuca.diet.model.Height;
import com.mumuca.diet.model.User;
import com.mumuca.diet.repository.HeightRepository;
import com.mumuca.diet.repository.UserRepository;
import com.mumuca.diet.service.HeightService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class HeightServiceImpl implements HeightService {

    private final UserRepository userRepository;
    private final HeightRepository heightRepository;

    @Override
    public HeightDTO createHeightRegistry(HeightRegistryDTO heightRegistryDTO, String userId) {
        Height height = new Height();

        height.setRegistry(heightRegistryDTO.registry());
        height.setDateTime(heightRegistryDTO.dateTime());

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        height.setUser(user);

        Height heightSaved = heightRepository.save(height);

        return new HeightDTO(heightSaved.getId(), heightSaved.getRegistry(), heightSaved.getDateTime());
    }
}
