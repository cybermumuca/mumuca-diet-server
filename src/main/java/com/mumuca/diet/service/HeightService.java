package com.mumuca.diet.service;

import com.mumuca.diet.dto.*;

public interface HeightService {
    HeightDTO createHeightRegistry(HeightRegistryDTO heightRegistryDTO, String userId);
    HeightDTO getHeightRegistry(String heightId, String userId);
    HeightDTO updateHeightRegistry(String heightId, HeightUpdateDTO heightUpdateDTO, String userId);
    void deleteHeightRegistry(String heightId, String userId);
}
