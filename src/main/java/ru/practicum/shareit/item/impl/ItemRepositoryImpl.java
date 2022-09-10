package ru.practicum.shareit.item.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.WrongParameterException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class ItemRepositoryImpl implements ItemRepository {
    Map<Long, Item> items = new HashMap<>();
    private long id = 0;

    @Override
    public Item create(Item item) {
        item.setId(getId());
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item findItemById(long itemId) {
        if (!items.containsKey(itemId)) {
            throw new WrongParameterException("ID вещи указан не верно");
        }
        return items.get(itemId);
    }

    @Override
    public List<Item> findItemAll(long userId) {
        return items.values().stream().filter(item -> item.getOwner().getId() == userId).collect(Collectors.toList());
    }

    @Override
    public Item update(long userId, Item item) {
        if (items.get(item.getId()).getOwner().getId() != userId) {
            throw new WrongParameterException("Редактирование не доступно. Нет прав");
        }
        Item itemUpdate = findItemById(item.getId());

        if (item.getName() != null) {
            itemUpdate.setName(item.getName());
        }

        if (item.getDescription() != null) {
            itemUpdate.setDescription(item.getDescription());
        }

        if (item.getAvailable() != null) {
            itemUpdate.setAvailable(item.getAvailable());
        }
        return itemUpdate;
    }

    @Override
    public Collection<Item> searchItem(String text) {
        return items.values().stream()
                .filter(item -> (item.getName().toLowerCase().contains(text.trim().toLowerCase())
                        || item.getDescription().toLowerCase().contains(text.trim().toLowerCase()))
                        && !text.trim().isEmpty())
                .filter(Item::getAvailable)
                .collect(Collectors.toList());
    }

    @Override
    public Item delete(long userId, long itemId) {
        Item item = findItemById(itemId);
        if (item.getOwner().getId() != userId) {
            throw new WrongParameterException("Редактирование не доступно. Нет прав");
        }
        return items.remove(id);
    }

    @Override
    public boolean deleteItemsByIdUser(long userId) {
        items.values().stream()
                .filter(item -> item.getOwner().getId() == userId)
                .map(item -> this.delete(item.getOwner().getId(), item.getId()))
                .count();
        return true;
    }

    private long getId() {
        return ++id;
    }
}
