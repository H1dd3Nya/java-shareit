package ru.practicum.shareit.item.dto.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "available", source = "item.isAvailable")
    ItemDto toItemDto(Item item);

    @Mapping(target = "id", source = "item.id")
    @Mapping(target = "available", source = "item.isAvailable")
    ItemDto toItemDto(Item item, List<CommentDto> comments);

    @Mapping(target = "id", source = "item.id")
    @Mapping(target = "available", source = "item.isAvailable")
    @Mapping(target = "lastBooking", source = "last")
    @Mapping(target = "nextBooking", source = "next")
    ItemDto toItemDto(Item item, BookingDto last, BookingDto next, List<CommentDto> comments);

    List<ItemDto> toDtoList(List<Item> items);
}
