package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NullFieldException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;


    @Override
    public ItemDto getById(Long id) {
        log.info("Read Item: {}", id);
        return itemMapper.toItemDto(itemRepository.read(id).orElseThrow(() -> new NotFoundException("Item not found")));
    }

    @Override
    public List<ItemDto> getAll(Long ownerId) {
        log.info("Getting all items for user with id {}", ownerId);

        userExistCheck(userRepository.getById(ownerId));
        return itemMapper.toDtoList(itemRepository.getAllByOwner(ownerId));
    }

    @Override
    public ItemDto create(ItemDto item, Long ownerId) {
        log.info("Checking if user with id={} is exist", ownerId);
        User owner = userExistCheck(userRepository.getById(ownerId));

        if (item.getAvailable() == null) {
            throw new NullFieldException("Available field is null");
        }

        if (item.getDescription() == null) {
            throw new NullFieldException("Description field is null");
        }

        log.info("Preparing new data");
        Item newItem = new Item();

        newItem.setOwner(owner);
        newItem.setName(item.getName());
        newItem.setDescription(item.getDescription());
        newItem.setIsAvailable(item.getAvailable());

        log.info("Calling repository method");
        return itemMapper.toItemDto(itemRepository.create(newItem));
    }

    @Override
    public ItemDto update(ItemDto item, Long itemId, Long ownerId) {
        log.info("Checking if user with id={} and item with id={} is exist", ownerId, itemId);
        User itemOwner = userExistCheck(userRepository.getById(ownerId));
        Item updateItem = itemRepository.read(itemId).orElseThrow(() -> new NotFoundException("Item not found"));

        log.info("Checking user permission");
        if (!updateItem.getOwner().getId().equals(itemOwner.getId())) {
            throw new AccessDeniedException("You do not have permission to update this item");
        }

        log.info("Editing data");
        updateItem.setName(item.getName());
        updateItem.setDescription(item.getDescription());
        updateItem.setIsAvailable(item.getAvailable());

        log.info("Calling repository method");
        return itemMapper.toItemDto(itemRepository.update(updateItem));
    }

    @Override
    public List<ItemDto> find(String text) {
        return itemMapper.toDtoList(itemRepository.findByName(text));
    }

    private User userExistCheck(Optional<User> user) {
        return user.orElseThrow(() -> new NotFoundException("User not found"));
    }
}
