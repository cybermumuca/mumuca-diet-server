package com.mumuca.diet.service.impl;

import com.mumuca.diet.dto.*;
import com.mumuca.diet.exception.ResourceNotFoundException;
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

    @Override
    public HeightDTO getHeightRegistry(String heightId, String userId) {
        return heightRepository.findByIdAndUserId(heightId, userId)
                .map((height -> new HeightDTO(height.getId(), height.getRegistry(), height.getDateTime())))
                .orElseThrow(() -> new ResourceNotFoundException("Height Registry not found."));
    }

    @Override
    public HeightDTO updateHeightRegistry(String heightId, HeightUpdateDTO heightUpdateDTO, String userId) {
        Height heightToUpdate = heightRepository.findByIdAndUserId(heightId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Height Registry not found."));

        if (heightUpdateDTO.registry() != null) {
            heightToUpdate.setRegistry(heightUpdateDTO.registry());
        }

        if (heightUpdateDTO.dateTime() != null) {
            heightToUpdate.setDateTime(heightUpdateDTO.dateTime());
        }

        heightRepository.save(heightToUpdate);

        return new HeightDTO(heightId, heightToUpdate.getRegistry(), heightToUpdate.getDateTime());
    }


    @Override
    public void deleteHeightRegistry(String heightId, String userId) {
        Height heightToDelete = heightRepository.findByIdAndUserId(heightId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Height Registry not found."));

        heightRepository.deleteById(heightToDelete.getId());
    }
}
