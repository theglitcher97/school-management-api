package com.school_management.api.utils;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class PasswordGenerator {
    private final String UPPER_CASE = "QWERTYUIOPASDFGHJKLZXCVBNM";
    private final String LOWER_CASE = "qwertyuiopasdfghjklzxcvbnm";
    private final String NUMBERS = "0123456789";
    private final List<String> availableCharacters = List.of(UPPER_CASE, LOWER_CASE, NUMBERS);
    private final Random random = new Random();
    private final int PASSWORD_SHUFFLE_ITERATIONS = 5;

    public String generatePassword(int length) {
        StringBuilder passwordBuilder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            String charactersSet = availableCharacters.get(random.nextInt(availableCharacters.size()));
            passwordBuilder.append(charactersSet.charAt(random.nextInt(charactersSet.length())));
        }

        for (int i = 0; i < PASSWORD_SHUFFLE_ITERATIONS; i++) {
            int randPos1 = random.nextInt(length);
            int randPos2 = random.nextInt(length);
            char c1 = passwordBuilder.charAt(randPos1);
            char c2 = passwordBuilder.charAt(randPos2);
            passwordBuilder.setCharAt(randPos1, c2);
            passwordBuilder.setCharAt(randPos2, c1);
        }

        return passwordBuilder.toString();
    }
}
