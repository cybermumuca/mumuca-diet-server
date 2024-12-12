package com.mumuca.diet.service;

import com.mumuca.diet.dto.body.BodyDTO;
import com.mumuca.diet.dto.body.BodyRegistryDTO;
import com.mumuca.diet.dto.body.BodyRegistryUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BodyService {
    BodyDTO registerBody(BodyRegistryDTO bodyRegistryDTO, String userId);
    BodyDTO getBodyRegistry(String bodyId, String userId);
    void updateBodyRegistry(String bodyId, BodyRegistryUpdateDTO bodyRegistryUpdateDTO, String userId);
    void deleteBodyRegistry(String bodyId, String subject);
    Page<BodyDTO> getBodiesRegistry(Pageable pageable, String userId);
}
