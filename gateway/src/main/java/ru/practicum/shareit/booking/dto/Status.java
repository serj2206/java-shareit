package ru.practicum.shareit.booking.dto;

public enum Status {
    // Ожидающие подтверждения
    WAITING,
    // Утвержденные
    APPROVED,
    // Бронирование отклонено владельцем,
    REJECTED,
    // Бронирование отменено создателем
    CANCELED
}

