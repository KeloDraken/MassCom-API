package com.example.accessingdatajpa.dto;

public record EmailMessageDTO(Long from, String subject, String body, Long property) {
}
