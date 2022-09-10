package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;

public interface ItemRepository {

    Item create(Item item);

    Item findItemById(long itemId);

    List<Item> findItemAll(long userId);

    Item update(long userId, Item item);

    Collection<Item> searchItem(String text);

    Item delete(long userId, long itemId);

    boolean deleteItemsByIdUser(long userId);
}
