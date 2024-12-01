package com.mumuca.diet.service.impl;

import com.mumuca.diet.dto.WeightDTO;
import com.mumuca.diet.dto.WeightRegistryDTO;
import com.mumuca.diet.exception.ResourceNotFoundException;
import com.mumuca.diet.model.User;
import com.mumuca.diet.model.Weight;
import com.mumuca.diet.repository.UserRepository;
import com.mumuca.diet.repository.WeightRepository;
import com.mumuca.diet.service.WeightService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WeightServiceImpl implements WeightService {

    private final UserRepository userRepository;
    private final WeightRepository weightRepository;

    @Override
    public WeightDTO createRegistry(WeightRegistryDTO weightRegistryDTO, String userId) {
        Weight weight = new Weight();

        weight.setRegistry(weightRegistryDTO.registry());
        weight.setDateTime(weightRegistryDTO.dateTime());

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        weight.setUser(user);

        Weight weightSaved = weightRepository.save(weight);

        return new WeightDTO(weight.getId(), weightSaved.getRegistry(), weightSaved.getDateTime());
    }

    @Override
    public WeightDTO getRegistry(String weightId, String userId) {
        return weightRepository.findByIdAndUserId(weightId, userId)
                .map((weight -> new WeightDTO(weight.getId(), weight.getRegistry(), weight.getDateTime())))
                .orElseThrow(() -> new ResourceNotFoundException("Weight Registry not found."));
    }
}
