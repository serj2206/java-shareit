package ru.practicum.shareit.request.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.FromSizeRequest;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    //Добавление запросов
    @Override
    public ItemRequestDto addItemRequest(ItemRequestDto itemRequestDto, long userId) {

        User requestor = userRepository.findById(userId).orElseThrow();

        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);

        itemRequest.setRequestor(requestor);
        itemRequest.setCreated(LocalDateTime.now());

        return ItemRequestMapper.toItemRequestDto(itemRequestRepository.save(itemRequest));
    }

    //Выгрузка своих запросов
    @Override
    public List<ItemRequestDto> findItemRequest(long userId) {

        userRepository.findById(userId).orElseThrow();

        List<ItemRequest> itemRequestList = itemRequestRepository.findAllByRequestorId(userId);

        List<ItemRequestDto> itemRequestDtoList = itemRequestList.stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList()); //Сортировать по времени в БД

        for (ItemRequestDto itemRequestDto : itemRequestDtoList) {
            addItemToRequest(itemRequestDto);
        }
        return itemRequestDtoList;
    }

    //Добавление вещи к запросу - вспомогательный метод
    private ItemRequestDto addItemToRequest(ItemRequestDto itemRequestDto) {
        List<Item> itemList = itemRepository.findItemByRequestId(itemRequestDto.getId());
        for (Item item : itemList) {
            ItemRequestMapper.toItemRequestDto(itemRequestDto, item);
        }
        return itemRequestDto;
    }

    //Запросы других пользователей
    @Override
    public List<ItemRequestDto> findItemRequestByRequestorId(long userId, Integer from, Integer size) {

        userRepository.findById(userId).orElseThrow(() -> new BadRequestException("Нет прав"));
        Pageable pageable;
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        //Если размер страницы не указан выгрузить все
        if (size == null) {
            pageable = null;
        } else {
            //Постраничная выгрузка для конкретного пользователя
            if (from < 0 || size < 0) {
                throw new BadRequestException("Значения парметров не корректны");
            }

            pageable = FromSizeRequest.of(from, size, sort);
        }

        Page<ItemRequest> itemRequestList = itemRequestRepository.findByRequestorIdNot(userId, pageable);

        return itemRequestList.stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .map(this::addItemToRequest)
                .collect(Collectors.toList()); //Сортировать по времени в БД
    }

    //Поиск определенного запрса
    @Override
    public ItemRequestDto findItemById(long userId, long id) {
        userRepository.findById(userId).orElseThrow();
        ItemRequest itemRequest = itemRequestRepository.findById(id).orElseThrow();

        return addItemToRequest(ItemRequestMapper.toItemRequestDto(itemRequest));
    }
}
