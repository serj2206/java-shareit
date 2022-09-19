package ru.practicum.shareit.item.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemSevice;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemSevice {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ItemDto create(long userId, ItemDto itemDto) {
        User user = userRepository.findUserById(userId);
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(user);
        return ItemMapper.toItemDto(itemRepository.create(item));
    }

    @Override
    public ItemDto findItemDtoById(long itemId) {
        return ItemMapper.toItemDto(itemRepository.findItemById(itemId));
    }

    @Override
    public Collection<ItemDto> findItemDtoAll(long userId) {
        List<Item> items = new ArrayList<>(itemRepository.findItemAll(userId));
        return items.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto update(long userId, long itemId, ItemDto itemDto) {
        itemDto.setId(itemId);
        Item item = ItemMapper.toItem(itemDto);
        return ItemMapper.toItemDto(itemRepository.update(userId, item));
    }

    @Override
    public Collection<ItemDto> searchItem(String text) {
        return itemRepository.searchItem(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto delete(long userId, long itemId) {
        return ItemMapper.toItemDto(itemRepository.delete(userId, itemId));
    }

}
