package ru.practicum.shareit.request.dto.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemRequestMapper {
    @Mapping(target = "created", source = "itemRequest.createdAt")
    ItemRequestDto toDto(ItemRequest itemRequest);

    List<ItemRequestDto> toDtoList(List<ItemRequest> itemRequests);
}
