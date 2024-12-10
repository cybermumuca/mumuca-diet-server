package com.mumuca.diet.service;

import com.mumuca.diet.dto.progress.DailyProgressDTO;

import java.time.LocalDate;

public interface ProgressService {
    DailyProgressDTO getDailyProgress(LocalDate date, String userId);
}
