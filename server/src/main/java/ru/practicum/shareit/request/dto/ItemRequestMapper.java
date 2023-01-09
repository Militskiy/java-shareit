package ru.practicum.shareit.request.dto;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = {ItemMapper.class})
public interface ItemRequestMapper {
    ItemRequest toEntity(ItemRequestCreateDto itemRequestCreateDto);

    ItemRequestCreateResponseDto toCreateResponseDto(ItemRequest itemRequest);

    ItemRequestResponseDto toResponseDto(ItemRequest itemRequest);

    List<ItemRequestResponseDto> toResponseList(Page<ItemRequest> itemRequests);
}