package ru.practicum.shareit.booking.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("select b " +
            "from Booking as b " +
            "left join Item as i on b.id = i.id " +
            "where b.id = ?1 and i.owner.id = ?2 or b.id = ?1 and b.booker.id = ?2")
    Optional<Booking> findBooking(Long id, Long userId);

    Page<Booking> findBookingsByBookerIdOrderByStartDesc(Long userId, Pageable pageRequest);

    Page<Booking> findBookingsByItemOwnerIdOrderByStartDesc(Long userId, Pageable pageRequest);

    Page<Booking> findBookingsByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(
            Long userId, LocalDateTime now, LocalDateTime timeNow, Pageable pageRequest
    );

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

    Booking findFirstByItem_IdAndEndIsBeforeOrderByEndDesc(Long itemId, LocalDateTime now);

    Booking findFirstByItem_IdAndStartIsAfterOrderByStartAsc(Long itemId, LocalDateTime now);

    Boolean existsBookingByItem_IdAndBooker_IdAndStatusAndEndIsBefore(
            Long itemId, Long bookerId, Status status, LocalDateTime now
    );
}
