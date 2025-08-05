package com.mumuca.diet.progress.service;

import com.mumuca.diet.progress.dto.DailyProgressDTO;

import java.time.LocalDate;

public interface ProgressService {
    DailyProgressDTO getDailyProgress(LocalDate date, String userId);
}
