package com.mumuca.diet.profile.service;

import com.mumuca.diet.profile.dto.CompleteRegistrationDTO;
import com.mumuca.diet.profile.dto.DiagnosisDTO;
import com.mumuca.diet.profile.dto.ProfileDTO;
import com.mumuca.diet.profile.dto.RegistrationCompletedDTO;

public interface UserService {
    RegistrationCompletedDTO completeRegistration(CompleteRegistrationDTO completeRegistrationDTO, String userId);
    ProfileDTO getUserProfile(String userId);
    DiagnosisDTO generateDiagnosis(String userId);

    void resetRegister(String userId);
}
