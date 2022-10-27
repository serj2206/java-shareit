package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ItemDto {
    private Long id;
    private String name;
    private String description;
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
