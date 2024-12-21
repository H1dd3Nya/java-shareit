package ru.practicum.shareit.item.dto.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "available", source = "item.isAvailable")
    ItemDto toItemDto(Item item);

    List<ItemDto> toDtoList(List<Item> items);
}
