package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query(value = "select * from bookings " +
            "WHERE booker_id = ?1 AND status = ?2 " +
            "ORDER BY start_date DESC", nativeQuery = true)
    List<Booking> findAllBookingByBookerIdStatus(Long userId, String state);

    @Query(value = "select * from bookings " +
            "WHERE booker_id = ?1 " +
            "ORDER BY start_date DESC", nativeQuery = true)
    List<Booking> findAllBookingByBookerIdALL(Long userId);

    @Query(value = "select * from bookings " +
            "WHERE booker_id = ?1 AND (CURRENT_TIMESTAMP between start_date AND end_date) " +
            "ORDER BY start_date DESC", nativeQuery = true)
    List<Booking> findAllBookingByBookerIdCurrent(Long userId);

    @Query(value = "select * from bookings " +
            "WHERE booker_id = ?1 AND start_date > CURRENT_TIMESTAMP " +
            "ORDER BY start_date DESC", nativeQuery = true)
    List<Booking> findAllBookingByBookerIdFuture(Long userId);

    @Query(value = "select * from bookings " +
            "WHERE booker_id = ?1 AND end_date < CURRENT_TIMESTAMP AND status = 'APPROVED' " +
            "ORDER BY start_date DESC", nativeQuery = true)
    List<Booking> findAllBookingByBookerIdPast(Long userId);

    //Owner
    @Query(value = "select b.*  FROM bookings AS b LEFT OUTER JOIN items AS i ON b.item_id = i.id " +
            "WHERE i.owner_id = ?1 AND b.status = ?2 " +
            "ORDER BY start_date DESC", nativeQuery = true)
    List<Booking> findAllBookingByOwnerIdStatus(Long userId, String state);

    @Query(value = "select b.*  FROM bookings AS b LEFT OUTER JOIN items AS i ON b.item_id = i.id " +
            "WHERE i.owner_id = ?1 " +
            "ORDER BY start_date DESC", nativeQuery = true)
    List<Booking> findAllBookingByOwnerIdALL(Long userId);

    @Query(value = "select b.* from bookings AS b LEFT OUTER JOIN items AS i ON b.item_id = i.id " +
            "WHERE i.owner_id = ?1 AND (CURRENT_TIMESTAMP between b.start_date AND b.end_date) " +
            "ORDER BY b.start_date DESC", nativeQuery = true)
    List<Booking> findAllBookingByOwnerIdCurrent(Long userId);

    @Query(value = "select b.* from bookings AS b LEFT OUTER JOIN items AS i ON b.item_id = i.id " +
            "WHERE i.owner_id = ?1 AND b.start_date > CURRENT_TIMESTAMP " +
            "ORDER BY b.start_date DESC", nativeQuery = true)
    List<Booking> findAllBookingByOwnerIdFuture(Long userId);

    @Query(value = "select b.* from bookings AS b LEFT OUTER JOIN items AS i ON b.item_id = i.id " +
            "WHERE i.owner_id = ?1 AND b.end_date < CURRENT_TIMESTAMP AND b.status = 'APPROVED' " +
            "ORDER BY b.start_date DESC", nativeQuery = true)
    List<Booking> findAllBookingByOwnerIdPast(Long userId);

    @Query(value = "select b.* from " +
            "((SELECT b1.* from bookings AS b1 " +
            "WHERE  b1.item_id = ?1 AND status = 'APPROVED' " +
            "ORDER BY b1.start_date DESC " +
            "limit 1) " +
            "UNION ALL ( " +
            "SELECT b2.* from bookings AS b2 " +
            "WHERE  b2.item_id = ?1 AND status = 'APPROVED' " +
            "ORDER BY b2.start_date ASC " +
            "limit 1)) AS b ", nativeQuery = true)
    List<Booking> findBookingByNextAndLast(long itemId);

    @Query(value = "select count(b.id) " +
            "FROM bookings AS b " +
            "WHERE b.item_id = ?1 AND b.booker_id = ?2 " +
            "AND b.status = ?3 AND b.end_date < CURRENT_TIMESTAMP " +
            "GROUP BY b.id " +
            "ORDER BY b.id ", nativeQuery = true)
    Long findByItem_IdAndBooker_IdAndStatus(long itemId, long userId, String status);
}

