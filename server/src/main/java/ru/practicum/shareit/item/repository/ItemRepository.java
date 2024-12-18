package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByOwnerId(Long ownerId);

    List<Item> findAllByNameContainingIgnoreCaseAndIsAvailable(String text, boolean available);

    List<Item> findFirst20ByRequestIdOrderByIdDesc(Long requestId);

    @Query("select i from Item as i" +
            " where i.requestId in ?1")
    List<Item> getItemsByRequestIds(List<Long> requestIds);
}
