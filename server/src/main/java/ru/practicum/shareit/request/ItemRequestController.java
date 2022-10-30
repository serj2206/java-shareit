package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto addItemRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @RequestBody ItemRequestDto itemRequestDto) {
        log.info(" ItemRequestController: POST_request addItemRequest() userID = {}", userId);
        return itemRequestService.addItemRequest(itemRequestDto, userId);
    }

    //Выгрузка свои запросов
    @GetMapping
    public List<ItemRequestDto> findItemRequest(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info(" ItemRequestController: GET_request findItemRequest() userID = {}", userId);
        return itemRequestService.findItemRequest(userId);
    }

    //Выгрузка списка чужих запросов
    //from - индекс первого элемента
    //size - количество элементов для отображения
    @GetMapping("/all")
    public List<ItemRequestDto> findItemRequestByRequestorId(@RequestHeader("X-Sharer-User-Id") long userId,
                                                             @RequestParam(defaultValue = "0") Integer from,
                                                             @RequestParam(defaultValue = "10") Integer size) {
        log.info(" ItemRequestController: GET_request findItemRequestByRequestorId() userID = {}, from = {}, size ={}", userId, from, size);
        return itemRequestService.findItemRequestByRequestorId(userId, from, size);
    }

    //Выгрузка конкретного запроса
    @GetMapping("/{requestId}")
    public ItemRequestDto findItemRequestById(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @PathVariable long requestId) {
        log.info(" ItemRequestController: GET_request findItemRequestById() userID = {}, requestId = {}", userId, requestId);
        return itemRequestService.findItemById(userId, requestId);
    }
}
