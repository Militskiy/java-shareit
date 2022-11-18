package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.model.Item;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ItemMapper {
    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    ItemDto itemToItemDto(Item item);

    Item itemDtoToItem(ItemDto itemDto);

    void updateItemFromDto(ItemDto itemDto, @MappingTarget Item item);
}
