package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto addItemRequest(ItemRequestDto itemRequestDto, long userId);

    List<ItemRequestDto> findItemRequest(long userId);


    List<ItemRequestDto> findItemRequestByRequestorId(long userId, Integer from, Integer size);

    ItemRequestDto findItemById(long userId, long id);
}
