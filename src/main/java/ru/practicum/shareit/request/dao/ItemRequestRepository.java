package ru.practicum.shareit.request.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.ItemRequest;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    Page<ItemRequest> findItemRequestsByRequester_Id(Long userId, Pageable pageable);

    Page<ItemRequest> findItemRequestsByRequester_IdIsNot(Long userId, Pageable pageable);
}