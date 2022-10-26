package ru.practicum.shareit.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Page<Item> findItemByOwnerId(long userId, Pageable pageable);

    @Query(value = "SELECT it.* " +
            "FROM items AS it " +
            "WHERE (it.name ilike concat ('%', ?1, '%') OR it.description ilike concat ('%', ?1, '%')) " +
            "AND it.available = true " +
            "ORDER BY it.id", nativeQuery = true)
    Page<Item> searchItem(String text, Pageable pageable);

    List<Item> findItemByRequestId(long id);




}
