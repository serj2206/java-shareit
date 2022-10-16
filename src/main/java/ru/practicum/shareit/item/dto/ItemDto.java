package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.marker.Create;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ItemDto {
    private long id;

    @NotNull(groups = {Create.class})
    @NotBlank(groups = {Create.class})
    private String name;

    @NotNull(groups = {Create.class})
    @NotBlank(groups = {Create.class})
    private String description;

    @AssertTrue(groups = {Create.class})
    @NotNull(groups = {Create.class})
    private Boolean available;

    private Booking lastBooking;
    private Booking nextBooking;

    private List<CommentDto> comments = new ArrayList<>();

    private Long requestId;

    public ItemDto(long id, String name, String description, Boolean available, Long requestId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.requestId = requestId;

    }

    @Getter
    @Setter
    public class Booking {
        long id;
        long bookerId;
    }
}
