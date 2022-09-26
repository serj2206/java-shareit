package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findItemByOwnerId(long userId);

    @Query(value = "select it.id, " +
            "it.name, it.description, it.available, it.owner_id, it.request_id " +
            "from items as it " +
            "WHERE (it.name ilike %?1% OR it.description ilike %?1% ) AND it.available = true " +
            "ORDER BY it.id", nativeQuery = true)
    Collection<Item> searchItem(String text);
}
