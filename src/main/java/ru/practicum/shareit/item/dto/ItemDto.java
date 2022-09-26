package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.marker.Create;
import ru.practicum.shareit.marker.Update;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
public class ItemDto {
    private long id;

    @NotNull(groups = {Create.class})
    @NotBlank(groups = {Create.class, Update.class})
    private String name;

    @NotNull(groups = {Create.class})
    @NotBlank(groups = {Create.class, Update.class})
    private String description;

    @AssertTrue(groups = {Create.class})
    @NotNull(groups = {Create.class})
    private Boolean available;

    private Booking lastBooking;
    private Booking nextBooking;

    private List<CommentDto> comments = new ArrayList<>();

    //Long requestId;

    public ItemDto(long id, String name, String description, Boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
    }

    @Getter
    @Setter
    public class Booking {
        long id;
        long bookerId;
    }
}
