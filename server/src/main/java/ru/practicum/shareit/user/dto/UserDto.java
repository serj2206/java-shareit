package ru.practicum.shareit.user.dto;


import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.marker.Create;
import ru.practicum.shareit.marker.Update;

@Data
@NoArgsConstructor
public class UserDto {
    @Null(groups = {Create.class})
    private Long id;

    @Pattern(regexp = "^\\S*$", groups = {Create.class, Update.class})
    @NotNull(groups = {Create.class})
    @NotBlank(groups = {Create.class})
    private String name;

    @Email(groups = {Create.class, Update.class})
    @NotNull(groups = {Create.class})
    @NotBlank(groups = {Create.class})
    private String email;

    public UserDto(long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
