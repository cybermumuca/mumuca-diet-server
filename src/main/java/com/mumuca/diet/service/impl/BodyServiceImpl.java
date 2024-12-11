package com.mumuca.diet.service.impl;

import com.mumuca.diet.dto.body.BodyDTO;
import com.mumuca.diet.dto.body.BodyRegistryDTO;
import com.mumuca.diet.dto.body.BodyRegistryUpdateDTO;
import com.mumuca.diet.exception.ResourceNotFoundException;
import com.mumuca.diet.model.Body;
import com.mumuca.diet.model.User;
import com.mumuca.diet.repository.BodyRepository;
import com.mumuca.diet.repository.UserRepository;
import com.mumuca.diet.service.BodyService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static com.mumuca.diet.util.UpdateUtils.updateIfDifferent;

@Service
@AllArgsConstructor
public class BodyServiceImpl implements BodyService {

    private final BodyRepository bodyRepository;

    @Override
    public BodyDTO registerBody(BodyRegistryDTO bodyRegistryDTO, String userId) {
        User user = new User(userId);

        Body body = new Body();
        body.setHeight(bodyRegistryDTO.height());
        body.setWeight(bodyRegistryDTO.weight());
        body.setDate(bodyRegistryDTO.date());
        body.setUser(user);

        bodyRepository.save(body);

        return new BodyDTO(body.getId(), bodyRegistryDTO.height(), bodyRegistryDTO.weight(), bodyRegistryDTO.date());
    }

    @Override
    public BodyDTO getBodyRegistry(String bodyId, String userId) {
        return bodyRepository.findByIdAndUserId(bodyId, userId)
                .map(body -> new BodyDTO(body.getId(), body.getWeight(), body.getHeight(), body.getDate()))
                .orElseThrow(() -> new ResourceNotFoundException("Body registry not found."));
    }

    @Override
    public void updateBodyRegistry(String bodyId, BodyRegistryUpdateDTO bodyRegistryUpdateDTO, String userId) {
        Body bodyRegistryToUpdate = bodyRepository.findByIdAndUserId(bodyId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Body registry not found."));

        boolean updated = false;

        updated |= updateIfDifferent(
                bodyRegistryToUpdate::getWeight,
                bodyRegistryToUpdate::setWeight,
                bodyRegistryUpdateDTO.weight()
        );

        updated |= updateIfDifferent(
              bodyRegistryToUpdate::getHeight,
              bodyRegistryToUpdate::setHeight,
              bodyRegistryUpdateDTO.height()
        );

        updated |= updateIfDifferent(
                bodyRegistryToUpdate::getDate,
                bodyRegistryToUpdate::setDate,
                bodyRegistryUpdateDTO.date()
        );

        if (updated) {
            bodyRepository.save(bodyRegistryToUpdate);
        }
    }

    @Override
    public void deleteBodyRegistry(String bodyId, String userId) {
        Body bodyRegistryToDelete = bodyRepository.findByIdAndUserId(bodyId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Body registry not found."));

        bodyRepository.deleteById(bodyRegistryToDelete.getId());
    }
}
