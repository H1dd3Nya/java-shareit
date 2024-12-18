package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.annotation.Create;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    public static final String SHARER_USER_ID = "X-Sharer-User-Id";
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Validated(Create.class) ItemDto itemDto,
                                         @RequestHeader(SHARER_USER_ID) Long ownerId) {
        log.info("POST create item: {}", itemDto);
        return itemClient.createItem(ownerId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestBody @Valid CommentDto comment,
                                             @PathVariable Long itemId,
                                             @RequestHeader(SHARER_USER_ID) Long authorId) {
        log.info("POST add comment={} to item: {} from: {}", comment, itemId, authorId);
        return itemClient.addComment(authorId, itemId, comment);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getById(@PathVariable Long itemId,
                                          @RequestHeader(SHARER_USER_ID) Long userId) {
        log.info("GET read item: {}", itemId);
        return itemClient.getItemById(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestHeader(SHARER_USER_ID) Long ownerId) {
        log.info("GET read all items: {}", ownerId);
        return itemClient.getAllUserItems(ownerId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@PathVariable Long itemId,
                                         @RequestBody @Valid ItemDto itemDto,
                                         @RequestHeader(SHARER_USER_ID) Long ownerId) {
        log.info("PATCH update item: {}, itemId={}, ownerId={}", itemDto, itemId, ownerId);
        return itemClient.updateItem(ownerId, itemId, itemDto);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam String text) {
        log.info("GET search items: {}", text);
        return itemClient.searchItem(text);
    }
}
