package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.marker.Create;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/items")
@Validated
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") long userId,
                          @Validated({Create.class}) @RequestBody ItemDto itemDto) {
        log.info("ItemController: POST_request, create(), userId = {}, itemDto = {}", userId, itemDto);
        if (userId <= 0) throw new ValidationException("ID должен быть положительным");
        return itemService.create(userId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") long userId,
                                 @PathVariable long itemId,
                                 @Validated({Create.class}) @RequestBody CommentDto commentDto) {
        log.info("ItemController: POST_request, addComment(), userId = {}, itemId = {}, commentDto = {}", userId, itemId, commentDto);
        if (itemId <= 0 || userId <= 0) throw new ValidationException("ID должен быть положительным");
        return itemService.addComment(userId, itemId, commentDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto findItemDtoById(@RequestHeader("X-Sharer-User-Id") long userId,
                                   @PathVariable long itemId) {
        log.info("ItemController: GET_request, findItemDtoById(), userId = {}, itemId = {}", userId, itemId);
        if (itemId <= 0 || userId <= 0) throw new ValidationException("ID должен быть положительным");
        return itemService.findItemDtoById(itemId, userId);
    }

    @GetMapping
    public Collection<ItemDto> findItemDtoAll(@RequestHeader("X-Sharer-User-Id") long userId,

                                              @PositiveOrZero(message = "from не должен быть отрицательным")
                                              @RequestParam(defaultValue = "0") Integer from,

                                              @Positive(message = "size должен быть положительным")
                                              @RequestParam(required = false) Integer size) {
        log.info("ItemController: GET_request, findItemDtoAll(), userID = {}, from = {}, size = {}", userId, from, size);
        if (userId <= 0) throw new ValidationException("ID должен быть положительным");
        return itemService.findItemDtoAll(userId, from, size);
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItems(@RequestHeader("X-Sharer-User-Id") long userId,
                                           @RequestParam String text,

                                           @PositiveOrZero(message = "from не должен быть отрицательным")
                                           @RequestParam(defaultValue = "1") Integer from,

                                           @Positive(message = "size должен быть положительным")
                                           @RequestParam(required = false) Integer size) {
        log.info("ItemController: GET_request, searchItems(), userId = {}, с параметрами text = {}, from = {}, size = {}", userId, text, from, size);
        if (userId <= 0) throw new ValidationException("ID должен быть положительным");
        return itemService.searchItem(text, from, size);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") long userId,
                          @PathVariable long itemId,
                          @RequestBody ItemDto itemDto) {
        log.info("ItemController: PATCH_request, update(), userId = {}, itemId = {}, itemDto = {}", userId, itemId, itemDto);
        if (itemId <= 0 || userId <= 0) throw new ValidationException("ID вещи должен быть положительным");
        return itemService.update(userId, itemId, itemDto);
    }

    @DeleteMapping("/{itemId}")
    public ItemDto delete(@RequestHeader("X-Sharer-User-Id") long userId,
                          @PathVariable long itemId) {
        log.info("ItemController: DELETE_request, delete(), userId = {}, itemId = {}", userId, itemId);
        if (itemId <= 0 || userId <= 0) throw new ValidationException("ID должен быть положительным");
        return itemService.delete(userId, itemId);
    }
}
