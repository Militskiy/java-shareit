package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestCreateResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDtoList;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRequestMapper mapper;

    @Override
    public ItemRequestCreateResponseDto create(ItemRequestCreateDto createDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("No user with ID: " + userId)
        );
        ItemRequest itemRequest = mapper.toEntity(createDto);
        itemRequest.setRequester(user);
        return mapper.toCreateResponseDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public ItemRequestResponseDtoList findOwnRequests(Long userId, Pageable pageable) {
        if (userRepository.existsById(userId)) {
            return ItemRequestResponseDtoList.builder()
                    .itemRequests(mapper.toResponseList(
                            itemRequestRepository.findItemRequestsByRequester_Id(userId, pageable)
                    ))
                    .build();
        } else {
            throw new NotFoundException("No user with ID: " + userId);
        }
    }

    @Override
    public ItemRequestResponseDtoList findAllRequests(Long userId, Pageable pageable) {
        if (userRepository.existsById(userId)) {
            return ItemRequestResponseDtoList.builder()
                    .itemRequests(mapper.toResponseList(
                            itemRequestRepository.findItemRequestsByRequester_IdIsNot(userId, pageable)
                    ))
                    .build();
        } else {
            throw new NotFoundException("No user with ID: " + userId);
        }
    }

    @Override
    public ItemRequestResponseDto findById(Long requestId, Long userId) {
        if (userRepository.existsById(userId)) {
            return mapper.toResponseDto(itemRequestRepository.findById(requestId).orElseThrow(
                    () -> new NotFoundException("No item request with ID: " + requestId)
            ));
        } else {
            throw new NotFoundException("No user with ID: " + userId);
        }
    }
}
