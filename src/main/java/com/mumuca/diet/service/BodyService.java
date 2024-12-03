package com.mumuca.diet.service;

import com.mumuca.diet.dto.body.BodyDTO;
import com.mumuca.diet.dto.body.BodyRegistryDTO;
import com.mumuca.diet.dto.body.BodyRegistryUpdateDTO;

public interface BodyService {
    BodyDTO registerBody(BodyRegistryDTO bodyRegistryDTO, String userId);
    BodyDTO getBodyRegistry(String bodyId, String userId);
    void updateBodyRegistry(String bodyId, BodyRegistryUpdateDTO bodyRegistryUpdateDTO, String userId);
//    float getBMI(String bodyId, String userId);
//    float getBMR(String bodyId, String userId);
    void deleteBodyRegistry(String bodyId, String subject);
}
