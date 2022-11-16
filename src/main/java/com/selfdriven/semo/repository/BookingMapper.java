package com.selfdriven.semo.repository;

import com.selfdriven.semo.entity.Booking;
import com.selfdriven.semo.entity.Reservation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Mapper
@Repository
public interface BookingMapper {
    Integer addBooking(Booking booking);
    Booking getBookingInfo(@Param("bookingId") int bookingId, @Param("memberId") String memberId);
    List<Booking> getBookingList(String memberId);
    Integer updateBooking(Booking booking);
    Integer deleteBooking(@Param("bookingId") int bookingId, @Param("memberId") String memberId);
    Integer deleteAllBooking();
}
