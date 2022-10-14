package ru.practicum.shareit.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findAllByRequestorId(long requestorId);

    //Page<ItemRequest> findAllByRequestorId(long requestorId, Pageable pageable);

    Page<ItemRequest> findByRequestorIdNot(long userId, Pageable pageable);
}
