package com.mumuca.diet.service;

import com.mumuca.diet.dto.CompleteRegistrationDTO;
import com.mumuca.diet.dto.DiagnosisDTO;
import com.mumuca.diet.dto.ProfileDTO;
import com.mumuca.diet.dto.RegistrationCompletedDTO;

public interface UserService {
    RegistrationCompletedDTO completeRegistration(CompleteRegistrationDTO completeRegistrationDTO, String userId);
    ProfileDTO getUserProfile(String userId);
    DiagnosisDTO generateDiagnosis(String userId);

    void resetRegister(String userId);
}
