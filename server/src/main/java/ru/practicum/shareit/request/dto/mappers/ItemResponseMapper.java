package ru.practicum.shareit.request.dto.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.request.dto.ItemResponseDto;
import ru.practicum.shareit.request.model.ItemResponse;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemResponseMapper {
    @Mapping(target = "id", source = "response.itemId")
    @Mapping(target = "name", source = "response.itemName")
    @Mapping(target = "ownerId", source = "response.itemOwnerId")
    ItemResponseDto toDto(ItemResponse response);

    List<ItemResponseDto> toDtoList(List<ItemResponse> responses);
}
