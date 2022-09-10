package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemSevice {
    ItemDto create(long userId, ItemDto itemDto);

    ItemDto findItemDtoById(long itemId);

    Collection<ItemDto> findItemDtoAll(long userId);

    ItemDto update(long userId, long itemId, ItemDto itemDto);

    Collection<ItemDto> searchItem(String text);

    ItemDto delete(long userId, long itemId);
}
