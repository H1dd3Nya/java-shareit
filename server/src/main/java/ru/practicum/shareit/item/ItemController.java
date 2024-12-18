package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    public static final String SHARER_USER_ID = "X-Sharer-User-Id";
    private final ItemService itemService;

    @PostMapping
    public ItemDto create(@RequestBody ItemDto itemDto, @RequestHeader(SHARER_USER_ID) Long ownerId) {
        log.info("POST create item: {}", itemDto);
        return itemService.create(itemDto, ownerId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestBody Comment comment,
                                 @PathVariable Long itemId,
                                 @RequestHeader(SHARER_USER_ID) Long authorId) {
        log.info("POST add comment={} to item: {} from: {}", comment, itemId, authorId);
        return itemService.addComment(comment, itemId, authorId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getById(@PathVariable Long itemId,
                           @RequestHeader(SHARER_USER_ID) Long userId) {
        log.info("GET read item: {}", itemId);
        return itemService.getById(itemId, userId);
    }

    @GetMapping
    public List<ItemDto> getAll(@RequestHeader(SHARER_USER_ID) Long ownerId) {
        log.info("GET read all items: {}", ownerId);
        return itemService.getAll(ownerId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@PathVariable Long itemId,
                          @RequestBody ItemDto itemDto,
                          @RequestHeader(SHARER_USER_ID) Long ownerId) {
        log.info("PATCH update item: {}, itemId={}, ownerId={}", itemDto, itemId, ownerId);
        return itemService.update(itemDto, itemId, ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        log.info("GET search items: {}", text);
        return itemService.find(text);
    }
}
