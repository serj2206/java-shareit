package ru.practicum.shareit.item.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.WrongParameterException;
import ru.practicum.shareit.item.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


@Service
public class ItemServiceImpl implements ItemSevice {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository, BookingRepository bookingRepository, CommentRepository commentRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
    }

    @Transactional
    @Override
    public ItemDto create(long userId, ItemDto itemDto) {
        User user = userRepository.findById(userId).orElseThrow();
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(user);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Transactional(readOnly = true)
    @Override
    public ItemDto findItemDtoById(long itemId, long userId) {
        Item item = itemRepository.findById(itemId).orElseThrow();
        ItemDto itemDto = ItemMapper.toItemDto(item);
        if (userId == item.getOwner().getId()) {
            itemDto = findBookingByNextAndLast(itemDto);
        }
        itemDto.setComments(findCommentByItemId(itemId));
        return itemDto;
    }

    @Override
    @Transactional(readOnly = true)
    public ItemDto findBookingByNextAndLast(ItemDto itemDto) {
        List<Booking> list = bookingRepository.findBookingByNextAndLast(itemDto.getId());

        if (!list.isEmpty()) {

            itemDto.setNextBooking(itemDto.new Booking());
            itemDto.getNextBooking().setId(list.get(0).getId());
            itemDto.getNextBooking().setBookerId(list.get(0).getBooker().getId());

            itemDto.setLastBooking(itemDto.new Booking());
            itemDto.getLastBooking().setId(list.get(1).getId());
            itemDto.getLastBooking().setBookerId(list.get(1).getBooker().getId());
        }
        return itemDto;
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<ItemDto> findItemDtoAll(long userId) {
        Collection<Item> items = new ArrayList<>(itemRepository.findItemByOwnerId(userId));
        Collection<ItemDto> itemDtoList = items.stream()
                .sorted((item1, item2) -> (int) (item1.getId() - item2.getId()))
                .map(ItemMapper::toItemDto)
                .map(this::findBookingByNextAndLast)
                .collect(Collectors.toList());

        itemDtoList.forEach(itemDto -> itemDto.setComments(findCommentByItemId(itemDto.getId())));

        return itemDtoList;
    }

    @Transactional()
    @Override
    public ItemDto update(long userId, long itemId, ItemDto itemDto) {
        itemDto.setId(itemId);

        Item itemUpdate = ItemMapper.toItem(itemDto);
        Item itemFromDb = itemRepository.findById(itemId).orElseThrow();

        if (itemFromDb.getOwner().getId() != userId) {
            throw new WrongParameterException("Редактирование не доступно. Нет прав");
        }

        if (itemUpdate.getName() != null) {
            itemFromDb.setName(itemUpdate.getName());
        }
        if (itemUpdate.getDescription() != null) {
            itemFromDb.setDescription(itemUpdate.getDescription());
        }
        if (itemUpdate.getAvailable() != null) {
            itemFromDb.setAvailable(itemUpdate.getAvailable());
        }
        if (itemFromDb.getRequest() != null) {
            itemFromDb.setRequest(itemUpdate.getRequest());
        }

        return ItemMapper.toItemDto(itemRepository.save(itemFromDb));
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<ItemDto> searchItem(String text) {
        if (text.isEmpty()) return new ArrayList<>();
        return itemRepository.searchItem(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Transactional()
    @Override
    public ItemDto delete(long userId, long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow();
        itemRepository.delete(item);
        return ItemMapper.toItemDto(item);
    }

    @Transactional
    @Override
    public CommentDto addComment(long userId, long itemId, CommentDto commentDto) {
        Long countBooking = bookingRepository.findByItem_IdAndBooker_IdAndStatus(itemId, userId, "APPROVED");
        if (countBooking == null || countBooking == 0L) {
            throw new BadRequestException("Нет прав для комментирования этой вещи");
        }

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NoSuchElementException("itemId = " + itemId + " не найдено"));
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("userId = " + userId + " не найдено"));
        LocalDateTime created = LocalDateTime.now();

        Comment comment = CommentMapper.toComment(commentDto.getText(), item, author, created);
        comment = commentRepository.save(comment);
        return CommentMapper.toCommentDto(comment);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> findCommentByItemId(Long itemId) {
        List<Comment> commentList = commentRepository.findCommentByItemId(itemId);
        List<CommentDto> commentDtoList = commentList.stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
        return commentDtoList;
    }

}
