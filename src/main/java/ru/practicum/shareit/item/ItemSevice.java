package ru.practicum.shareit.item;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemSevice {
    ItemDto create(long userId, ItemDto itemDto);

    ItemDto findItemDtoById(long itemId, long userId);

    @Transactional(readOnly = true)
    ItemDto findBookingByNextAndLast(ItemDto itemDto);

    Collection<ItemDto> findItemDtoAll(long userId);

    ItemDto update(long userId, long itemId, ItemDto itemDto);

    Collection<ItemDto> searchItem(String text);

    ItemDto delete(long userId, long itemId);

    CommentDto addComment(long userId, long itemId, CommentDto commentDto);

    @Transactional
    Collection<CommentDto> findCommentByItemId(Long itemId);
}
