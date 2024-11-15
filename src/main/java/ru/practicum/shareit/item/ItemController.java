package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto create(@RequestBody @Valid ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("POST create item: {}", itemDto);
        return itemService.create(itemDto, ownerId);
    }

    @GetMapping("/{itemId}")
    public ItemDto read(@PathVariable Long itemId) {
        log.info("GET read item: {}", itemId);
        return itemService.read(itemId);
    }

    @GetMapping
    public List<ItemDto> getAll(@RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("GET read all items: {}", ownerId);
        return itemService.getAll(ownerId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@PathVariable Long itemId,
                          @RequestBody @Valid ItemDto itemDto,
                          @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("PATCH update item: {}, itemId={}, ownerId={}", itemDto, itemId, ownerId);
        return itemService.update(itemDto, itemId, ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        log.info("GET search items: {}", text);
        return itemService.find(text);
    }
}
