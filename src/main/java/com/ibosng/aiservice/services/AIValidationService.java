package com.ibosng.aiservice.services;

import com.ibosng.aiservice.annotations.MethodDescription;

import java.util.List;

public interface AIValidationService {
    @MethodDescription("Validiert die Antwort für die Trainervertretungen. Es gibt eine Liste von möglichen Fehlern.")
    List<String> validateResponseForSeminarAndTrainers(String response);
}
