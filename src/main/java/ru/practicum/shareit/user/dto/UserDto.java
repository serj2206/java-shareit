package ru.practicum.shareit.user.dto;

import lombok.Data;
import ru.practicum.shareit.marker.Create;
import ru.practicum.shareit.marker.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class UserDto {

    long id;

    @Pattern(regexp = "^\\S*$", groups = {Create.class, Update.class})
    @NotNull(groups = {Create.class})
    @NotBlank(groups = {Create.class})
    String name;

    @Email(groups = {Create.class, Update.class})
    @NotNull(groups = {Create.class})

    String email;

    public UserDto(long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
