package ru.practicum.shareit.request.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.mappers.ItemShortMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.mappers.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemRequestMapper itemRequestMapper;
    private final ItemShortMapper itemShortMapper;

    @Override
    public ItemRequestDto createItemRequest(ItemRequestDto requestDto, Long userId) {
        User user = getUser(userId);

        ItemRequest request = new ItemRequest();
        request.setDescription(requestDto.getDescription());
        request.setAuthor(user.getId());
        request.setCreatedAt(LocalDateTime.now());

        return itemRequestMapper.toDto(itemRequestRepository.save(request));
    }

    @Override
    public ItemRequestDto getItemRequestById(Long id) {
        ItemRequestDto requestDto = itemRequestMapper.toDto(getItemRequest(id));
        requestDto.setItems(itemShortMapper.toDtoList(itemRepository.findFirst20ByRequestIdOrderByIdDesc(requestDto.getId())));

        return requestDto;
    }

    @Override
    public List<ItemRequestDto> getByUserId(Long userId) {
        log.info("Starting getByUserId method");
        User user = getUser(userId);
        log.info("Getting requests by user");
        List<ItemRequestDto> requests = itemRequestMapper.toDtoList(itemRequestRepository
                .findFirst20ByAuthorOrderByCreatedAt(user.getId()));
        log.info("Requests = {}", requests);

        return mapResponseToRequests(requests);
    }

    @Override
    public List<ItemRequestDto> getAll() {
        log.info("Starting getAll method");
        List<ItemRequestDto> requests = itemRequestMapper.toDtoList(itemRequestRepository
                .findFirst20ByOrderByCreatedAtDesc());

        return mapResponseToRequests(requests);
    }

    private List<ItemRequestDto> mapResponseToRequests(List<ItemRequestDto> requests) {
        Map<Long, ItemRequestDto> requestMap = new HashMap<>();

        for (ItemRequestDto request : requests) {
            requestMap.put(request.getId(), request);
        }

        List<Item> items = itemRepository
                .getItemsByRequestIds(new ArrayList<>(requestMap.keySet()));

        for (Item item : items) {
            ItemRequestDto request = requestMap.get(item.getRequestId());
            if (request.getItems() == null) {
                request.setItems(new ArrayList<>());
            }

            requestMap.get(item.getRequestId()).getItems().add(itemShortMapper.toDto(item));
        }

        return new ArrayList<>(requestMap.values());
    }

    private User getUser(Long id) {
        log.info("Checking if user with id: {} exists", id);
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
    }

    private ItemRequest getItemRequest(Long id) {
        return itemRequestRepository.findById(id).orElseThrow(() -> new NotFoundException("Request not found"));
    }
}
