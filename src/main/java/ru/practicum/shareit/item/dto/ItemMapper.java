package ru.practicum.shareit.item.dto;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ItemMapper {

    Item itemDtoToItem(ItemDto itemDto);

    List<ResponseItemDto> mapResponse(Page<Item> items);

    List<ItemWithBookingsDto> mapWithBooking(Page<Item> items);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Item updateItemFromUpdateItemDto(UpdateItemDto updateItemDto, @MappingTarget Item item);

    ResponseItemDto itemToResponseItemDto(Item item);

    ItemWithBookingsDto itemToItemWithBookingsDto(Item item);
}
