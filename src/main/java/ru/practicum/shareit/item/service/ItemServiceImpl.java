package ru.practicum.shareit.item.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NullFieldException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.mappers.CommentMapper;
import ru.practicum.shareit.item.dto.mappers.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
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
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemMapper itemMapper;
    private final BookingMapper bookingMapper;
    private final CommentMapper commentMapper;

    @Override
    public ItemDto getById(Long id, Long userId) {
        log.info("Get Item: {}", id);
        Item item = getItem(id);
        User user = getUser(userId);

        List<CommentDto> comments = commentMapper.toDtoList(commentRepository.findAllByItemId(item.getId()));

        if (item.getOwner().equals(user)) {
            BookingDto last = bookingMapper.toBookingDto(bookingRepository
                    .findFirstByEndLessThanAndItemIdEquals(LocalDateTime.now(), item.getId()));

            BookingDto next = bookingMapper.toBookingDto(bookingRepository
                    .findFirstByStartGreaterThanAndItemIdEquals(LocalDateTime.now(), item.getId()));

            return itemMapper.toItemDto(item, last, next, comments);
        }

        return itemMapper.toItemDto(item, comments);
    }

    @Override
    public List<ItemDto> getAll(Long ownerId) {
        List<Item> items = itemRepository.findAllByOwnerId(ownerId);
        List<Long> itemIds = items.stream().map(Item::getId).toList();

        Map<Long, Map<String, BookingDto>> bookings = getBookingsForItems(itemIds);
        Map<Long, List<CommentDto>> comments = getCommentsForItems(itemIds);

        List<ItemDto> itemDtos = new ArrayList<>();
        for (Item item : items) {
            if (bookings.get(item.getId()) != null) {
                BookingDto last = bookings.get(item.getId()).get("last");
                BookingDto next = bookings.get(item.getId()).get("next");
                List<CommentDto> commentList = comments.get(item.getId());

                itemDtos.add(itemMapper.toItemDto(item, last, next, commentList));
                continue;
            }

            List<CommentDto> commentList = comments.get(item.getId());

            itemDtos.add(itemMapper.toItemDto(item, commentList));
        }

        return itemDtos;
    }

    private Map<Long, List<CommentDto>> getCommentsForItems(List<Long> itemIds) {
        List<CommentDto> comments = commentMapper.toDtoList(commentRepository.getAllCommentsByItemIds(itemIds));
        Map<Long, List<CommentDto>> commentsByItemIds = new HashMap<>();

        for (CommentDto comment : comments) {
            Long commentId = comment.getId();

            if (commentsByItemIds.get(commentId) == null) {
                commentsByItemIds.put(commentId, new ArrayList<>());
            }

            commentsByItemIds.get(commentId).add(comment);
        }

        return commentsByItemIds;
    }

    private Map<Long, Map<String, BookingDto>> getBookingsForItems(List<Long> itemIds) {
        List<BookingDto> bookings = bookingMapper.toListBookingDto(bookingRepository.getAllBookingsByItemIds(itemIds));
        Map<Long, Map<String, BookingDto>> bookingsByItemId = new HashMap<>();

        LocalDateTime now = LocalDateTime.now();

        for (BookingDto booking : bookings) {
            Long itemId = booking.getItemId();

            if (bookingsByItemId.get(itemId) == null) {
                bookingsByItemId.put(itemId, new HashMap<>());
            }

            if (bookingsByItemId.get(itemId).get("last") != null && booking.getEnd().isBefore(now)) {
                if (bookingsByItemId.get(itemId).get("last").getEnd().isBefore(booking.getEnd())) {
                    bookingsByItemId.get(itemId).put("last", booking);
                }
            } else {
                bookingsByItemId.get(itemId).put("last", booking);
            }

            if (bookingsByItemId.get(itemId).get("next") != null) {
                if (bookingsByItemId.get(itemId).get("next").getStart().isAfter(booking.getStart())) {
                    bookingsByItemId.get(itemId).put("next", booking);
                }
            } else {
                bookingsByItemId.get(itemId).put("next", booking);
            }

        }

        return bookingsByItemId;
    }

    @Override
    public ItemDto create(ItemDto item, Long ownerId) {
        log.info("Checking if user with id={} is exist", ownerId);
        User owner = getUser(ownerId);

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
        return itemMapper.toItemDto(itemRepository.save(newItem));
    }

    @Override
    public ItemDto update(ItemDto item, Long itemId, Long ownerId) {
        log.info("Checking if user with id={} and item with id={} is exist", ownerId, itemId);
        User itemOwner = getUser(ownerId);
        Item updatedItem = getItem(itemId);

        log.info("Checking user permission");
        if (!updatedItem.getOwner().getId().equals(itemOwner.getId())) {
            throw new AccessDeniedException("You do not have permission to update this item");
        }

        log.info("Editing data");
        if (item.getName() != null) {
            updatedItem.setName(item.getName());
        }

        if (item.getDescription() != null) {
            updatedItem.setDescription(item.getDescription());
        }

        if (item.getAvailable() != null) {
            updatedItem.setIsAvailable(item.getAvailable());
        }

        log.info("Calling repository method");
        return itemMapper.toItemDto(itemRepository.save(updatedItem));
    }

    @Override
    public List<ItemDto> find(String text) {
        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        }

        return itemMapper.toDtoList(itemRepository.findAllByNameContainingIgnoreCaseAndIsAvailable(text, true));
    }

    @Override
    public CommentDto addComment(Comment comment, Long itemId, Long authorId) {
        LocalDateTime now = LocalDateTime.now();
        User author = getUser(authorId);
        Item item = getItem(itemId);
        Booking booking = bookingRepository.findByItemAndBookerAndEndLessThan(item, author, now);

        if (booking == null) {
            throw new IllegalArgumentException("User didn't book this item");
        }

        comment.setItemId(itemId);
        comment.setAuthor(author);
        comment.setCreatedAt(now);

        return commentMapper.toDto(commentRepository.save(comment));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
    }

    private Item getItem(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Item not found"));
    }
}
