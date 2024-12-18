package ru.practicum.shareit.request.dto.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemResponse;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemToItemResponseMapper {
    @Mapping(source = "item.id", target = "itemId")
    @Mapping(source = "item.name", target = "itemName")
    @Mapping(source = "item.owner.id", target = "itemOwnerId")
    ItemResponse toItemResponse(Item item);

    List<ItemResponse> toItemResponses(List<Item> items);
}
