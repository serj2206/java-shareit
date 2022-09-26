package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.marker.Create;
import ru.practicum.shareit.marker.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class CommentDto {

    private Long id;
    @NotNull(groups = {Create.class})
    @NotBlank(groups = {Create.class, Update.class})
    private String text;

    private String authorName;

    private LocalDateTime created;

    public CommentDto(Long id, String text, String authorName, LocalDateTime created) {
        this.id = id;
        this.text = text;
        this.authorName = authorName;
        this.created = created;
    }
}
