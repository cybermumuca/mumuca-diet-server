package com.mumuca.diet.service;

import com.mumuca.diet.dto.HeightDTO;
import com.mumuca.diet.dto.HeightRegistryDTO;

public interface HeightService {
    HeightDTO createHeightRegistry(HeightRegistryDTO heightRegistryDTO, String userId);
}
