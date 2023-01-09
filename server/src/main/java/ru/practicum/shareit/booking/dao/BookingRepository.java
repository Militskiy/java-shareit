package ru.practicum.shareit.booking.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findBookingByIdAndBooker_IdOrIdAndItem_Owner_Id(
            Long id, Long bookerId, Long bookingId, Long ownerId);

    Page<Booking> findBookingsByBookerIdOrderByStartDesc(Long userId, Pageable pageRequest);

    Page<Booking> findBookingsByItemOwnerIdOrderByStartDesc(Long userId, Pageable pageRequest);

    Page<Booking> findBookingsByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(
            Long userId, LocalDateTime now, LocalDateTime timeNow, Pageable pageRequest);

    Page<Booking> findBookingsByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(
            Long userId, LocalDateTime now, LocalDateTime timeNow, Pageable pageRequest
    );

    Page<Booking> findBookingsByBookerIdAndEndIsBeforeOrderByStartDesc(
            Long userId, LocalDateTime now, Pageable pageRequest
    );

    Page<Booking> findBookingsByItemOwnerIdAndEndIsBeforeOrderByStartDesc(
            Long userId, LocalDateTime now, Pageable pageRequest
    );

    Page<Booking> findBookingsByBookerIdAndStartIsAfterOrderByStartDesc(
            Long userId, LocalDateTime now, Pageable pageRequest
    );

    Page<Booking> findBookingsByItemOwnerIdAndStartIsAfterOrderByStartDesc(
            Long userId, LocalDateTime now, Pageable pageRequest
    );

    Page<Booking> findBookingsByBookerIdAndStatusIs(Long userId, Status status, Pageable pageRequest);

    Page<Booking> findBookingsByItemOwnerIdAndStatusIs(Long userId, Status status, Pageable pageRequest);

    Optional<Booking> findFirstByItem_IdAndStatusAndStartIsBeforeOrderByStartDesc(Long itemId, Status status, LocalDateTime now);

    Optional<Booking> findFirstByItem_IdAndStatusAndStartIsAfterOrderByStartAsc(Long itemId, Status status, LocalDateTime now);

    Boolean existsBookingByItem_IdAndBooker_IdAndStatusAndEndIsBefore(
            Long itemId, Long bookerId, Status status, LocalDateTime now
    );
}
