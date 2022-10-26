package ru.practicum.shareit.booking.dto;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class BookingDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Item item;
    private Booker booker;
    private Status status;

    public BookingDto(Long id,
                      LocalDateTime start,
                      LocalDateTime end,
                      Long itemId,
                      String itemName,
                      Long bookerId,
                      Status status) {
        this.item = new Item();
        this.booker = new Booker();
        this.id = id;
        this.start = start;
        this.end = end;
        this.item.setId(itemId);
        this.item.setName(itemName);
        this.booker.setId(bookerId);
        this.status = status;
    }

    public BookingDto() {
        this.item = new Item();
        this.booker = new Booker();
    }

    public void setItemId(Long itemId) {
        this.item.setId(itemId);
    }

    public void setStart(String string) {
        this.start = LocalDateTime.parse(string);
    }

    public void setEnd(String string) {
        this.end = LocalDateTime.parse(string);
    }

    @Data
    public class Item {
        private Long id;
        private String name;
    }

    @Data
    public class Booker {
        private Long id;
    }
}
