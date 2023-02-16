package com.example.accessingdatajpa.dto;

public record EmailMessageDTO(Long from, Long to, String subject, String body, Long property) {
}
