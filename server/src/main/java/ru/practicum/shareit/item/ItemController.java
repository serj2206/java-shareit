package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

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
                          @RequestBody ItemDto itemDto) {
        log.info("ItemController: POST_request, create(), userId = {}, itemDto = {}", userId, itemDto);
        return itemService.create(userId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") long userId,
                                 @PathVariable long itemId,
                                 @RequestBody CommentDto commentDto) {
        log.info("ItemController: POST_request, addComment(), userId = {}, itemId = {}, commentDto = {}", userId, itemId, commentDto);
        return itemService.addComment(userId, itemId, commentDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto findItemDtoById(@RequestHeader("X-Sharer-User-Id") long userId,
                                   @PathVariable long itemId) {
        log.info("ItemController: GET_request, findItemDtoById(), userId = {}, itemId = {}", userId, itemId);
        return itemService.findItemDtoById(itemId, userId);
    }

    @GetMapping
    public Collection<ItemDto> findItemDtoAll(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestParam(defaultValue = "0") Integer from,
                                              @RequestParam(defaultValue = "10") Integer size) {
        log.info("ItemController: GET_request, findItemDtoAll(), userID = {}, from = {}, size = {}", userId, from, size);
        return itemService.findItemDtoAll(userId, from, size);
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItems(@RequestHeader("X-Sharer-User-Id") long userId,
                                           @RequestParam String text,
                                           @RequestParam(defaultValue = "0") Integer from,
                                           @RequestParam(defaultValue = "10") Integer size) {
        log.info("ItemController: GET_request, searchItems(), userId = {}, с параметрами text = {}, from = {}, size = {}", userId, text, from, size);
        return itemService.searchItem(text, from, size);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") long userId,
                          @PathVariable long itemId,
                          @RequestBody ItemDto itemDto) {
        log.info("ItemController: PATCH_request, update(), userId = {}, itemId = {}, itemDto = {}", userId, itemId, itemDto);
        return itemService.update(userId, itemId, itemDto);
    }

    @DeleteMapping("/{itemId}")
    public ItemDto delete(@RequestHeader("X-Sharer-User-Id") long userId,
                          @PathVariable long itemId) {
        log.info("ItemController: DELETE_request, delete(), userId = {}, itemId = {}", userId, itemId);
        return itemService.delete(userId, itemId);
    }
}
