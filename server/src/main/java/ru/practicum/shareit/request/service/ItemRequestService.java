package ru.practicum.shareit.request.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestCreateResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDtoList;

@Service
public interface ItemRequestService {
    ItemRequestCreateResponseDto create(ItemRequestCreateDto createDto, Long userId);

    ItemRequestResponseDtoList findOwnRequests(Long userId, Pageable pageable);

    ItemRequestResponseDtoList findAllRequests(Long userId, Pageable pageable);

    ItemRequestResponseDto findById(Long requestId, Long userId);
}
