package com.mumuca.diet.service;

import com.mumuca.diet.dto.WeightDTO;
import com.mumuca.diet.dto.WeightRegistryDTO;
import com.mumuca.diet.dto.WeightUpdateDTO;

public interface WeightService {
    WeightDTO createRegistry(WeightRegistryDTO weightRegistryDTO, String userId);
    WeightDTO getRegistry(String weightId, String userId);
    WeightDTO updateRegistry(String weightId, WeightUpdateDTO weightUpdateDTO, String userId);
}
