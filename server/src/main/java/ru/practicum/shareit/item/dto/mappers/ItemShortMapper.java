package ru.practicum.shareit.item.dto.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemShortMapper {
    @Mapping(target = "id", source = "item.id")
    @Mapping(target = "name", source = "item.name")
    @Mapping(target = "ownerId", source = "item.owner.id")
    ItemShortDto toDto(Item item);

    List<ItemShortDto> toDtoList(List<Item> responses);
}
