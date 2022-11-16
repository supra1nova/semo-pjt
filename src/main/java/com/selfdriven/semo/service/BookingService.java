package com.selfdriven.semo.service;

import com.selfdriven.semo.entity.Booking;
import com.selfdriven.semo.entity.Reservation;
import com.selfdriven.semo.enums.ResultCode;
import com.selfdriven.semo.exception.ApiException;
import com.selfdriven.semo.repository.BookingMapper;
import com.selfdriven.semo.repository.ReservationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@RequiredArgsConstructor
@Service
public class BookingService {
    private final BookingMapper bookingMapper;
    private final ReservationService reservationService;

    public int insertBooking(Booking booking){
        return bookingMapper.addBooking(booking);
    }

    public Booking getBookingInfo(int bookingId, String memberId){
        return bookingMapper.getBookingInfo(bookingId, memberId);
    }

    public List<Booking> getBookingList(String memberId){
        return bookingMapper.getBookingList(memberId);
    }

    // TODO: 확정예약에 대해 수정이 필요할까? 필요하다면 방 번호와 날짜의 유효성 검사 로직이 필요할 수도 있을듯?
    // TODO: null 체크를 entity에서도 하고 있고, sql 내부에서 하고 있는데, 프론트에서도 null체크를 한번 하고 통과할 예정 -> sql에 있는 조건문을 빼도 되는지? 아니면 빼는게 맞는지?
    public int updateBooking(Booking booking) {
        Map<String, String> map = new HashMap<>();
        map.put("roomId", String.valueOf(booking.getRoomId()));
        map.put("startAt", booking.getStartAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        map.put("endAt", booking.getEndAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        if(!reservationService.getAvailability(map)){
            throw new ApiException(ResultCode.INVALID_DATE_ERROR);
        }
        return bookingMapper.updateBooking(booking);
    }

    public int deleteBooking(int bookingId){
        return bookingMapper.deleteBooking(bookingId);
    }
}
