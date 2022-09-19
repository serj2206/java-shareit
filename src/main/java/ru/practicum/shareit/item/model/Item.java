package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class Item {
    long id;


    @NotNull(message = "Название не может быть пустым")
    @NotBlank(message = "Название не может быть пустым")
    String name;

    @NotNull(message = "Описание не может быть пустым")
    @NotBlank(message = "Описание не может быть пустым")
    String description;

    @AssertTrue
    Boolean available;

    User owner;
    ItemRequest request;
}

