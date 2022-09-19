package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.marker.Create;
import ru.practicum.shareit.marker.Update;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class ItemDto {
    private long id;

    @Pattern(regexp = "^\\S*$", groups = {Create.class, Update.class})
    @NotNull(groups = {Create.class})
    @NotBlank(groups = {Create.class, Update.class})
    private String name;


    @NotNull(groups = {Create.class})
    @NotBlank(groups = {Create.class, Update.class})
    private String description;

    @AssertTrue(groups = {Create.class})
    @NotNull(groups = {Create.class})
    private Boolean available;

   //Long requestId;

    public ItemDto(long id, String name, String description, Boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
    }
}
