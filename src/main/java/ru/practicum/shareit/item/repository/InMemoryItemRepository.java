package ru.practicum.shareit.item.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Repository
@Slf4j
public class InMemoryItemRepository implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private Long counter = 1L;


    @Override
    public Item create(Item item) {
        item.setId(counter);
        items.put(counter, item);
        counter++;

        log.info("Created new item: {}", item);
        return item;
    }

    @Override
    public Optional<Item> read(Long id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public List<Item> getAll() {
        return new ArrayList<>(items.values());
    }

    @Override
    public List<Item> getAllByOwner(Long ownerId) {
        return items.values().stream()
                .filter(item -> item.getOwner().getId().equals(ownerId))
                .toList();
    }

    @Override
    public Item update(Item item) {
        return items.put(item.getId(), item);
    }

    @Override
    public void delete(Item item) {
        items.remove(item.getId());
    }

    @Override
    public List<Item> findByName(String text) {
        List<Item> matchedItems = new ArrayList<>();

        for (Item item : items.values()) {
            if (item.getName() != null && item.getName().equalsIgnoreCase(text) && item.getIsAvailable()) {
                matchedItems.add(item);
            }
        }

        return matchedItems;
    }
}
