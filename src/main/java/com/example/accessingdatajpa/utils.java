package com.example.accessingdatajpa;

import java.util.Optional;

public class utils {
    public static Optional<Long> parseId(String id) {
        try {
            return Optional.of(Long.parseLong(id));
        } catch (NumberFormatException numberFormatException) {
            return Optional.empty();
        }
    }
}
