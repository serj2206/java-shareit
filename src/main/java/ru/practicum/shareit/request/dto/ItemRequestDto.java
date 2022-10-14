package ru.practicum.shareit.request.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.marker.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ItemRequestDto {
    private long id;
    @NotNull(groups = {Create.class})
    @NotBlank(groups = {Create.class})
    private String description;
    private long requestorId;
    private LocalDateTime created;

    private List<Item> items = new ArrayList<>();

    public ItemRequestDto(long id, String description, long requestorId, LocalDateTime created) {
        this.id = id;
        this.description = description;
        this.requestorId = requestorId;
        this.created = created;
    }

    public void addItem(long id, String name, String description, Boolean available, Long requestId) {
        Item item = new Item(id, name, description, available, requestId);
        items.add(item);
    }


    @Data
    @AllArgsConstructor
    private class Item {
        private long id;
        private String name;
        private String description;
        private Boolean available;
        private Long requestId;
    }

}
