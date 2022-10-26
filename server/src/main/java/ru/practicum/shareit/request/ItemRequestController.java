package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.marker.Create;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
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
                                         @Validated({Create.class})
                                         @RequestBody ItemRequestDto itemRequestDto) {
        if (userId <= 0) throw new ValidationException("ID должен быть положительным");
        return itemRequestService.addItemRequest(itemRequestDto, userId);
    }

    //Выгрузка свои запросов
    @GetMapping
    public List<ItemRequestDto> findItemRequest(@RequestHeader("X-Sharer-User-Id") long userId) {
        if (userId <= 0) throw new ValidationException("ID должен быть положительным");
        return itemRequestService.findItemRequest(userId);
    }

    //Выгрузка списка чужих запросов
    //from - индекс первого элемента
    //size - количество элементов для отображения
    @GetMapping("/all")
    public List<ItemRequestDto> findItemRequestByRequestorId(@RequestHeader("X-Sharer-User-Id") long userId,

                                                             @PositiveOrZero(message = "from не должен быть отрицательным")
                                                             @RequestParam(defaultValue = "0") Integer from,

                                                             @Positive(message = "size должен быть положительным")
                                                             @RequestParam(required = false) Integer size) {
        if (userId <= 0) throw new ValidationException("ID должен быть положительным");
        log.info("ItemRequestController: GET_request findItemRequestByRequestorId() userID = {}, from = {}, size ={}", userId, from, size);
        return itemRequestService.findItemRequestByRequestorId(userId, from, size);
    }

    //Выгрузка конкретного запроса
    @GetMapping("/{requestId}")
    public ItemRequestDto findItemRequestById(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @PathVariable long requestId) {
        if (userId <= 0) throw new ValidationException("ID должен быть положительным");
        return itemRequestService.findItemById(userId, requestId);
    }
}
