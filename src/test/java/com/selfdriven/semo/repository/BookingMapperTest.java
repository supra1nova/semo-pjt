package com.selfdriven.semo.repository;

import com.selfdriven.semo.entity.Booking;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class BookingMapperTest {

    @Autowired
    private BookingMapper bookingMapper;

    @Test
    public void 확약생성테스트() {
        bookingMapper.deleteAllBooking();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startAt = LocalDate.parse("2022-12-11", formatter);
        LocalDate endAt =  LocalDate.parse("2022-12-16", formatter);
        Booking booking = new Booking("2465994341", 1, 4, startAt, endAt);
        bookingMapper.addBooking(booking);
        assertThat(bookingMapper.getBookingList(booking.getMemberId()).size()).isEqualTo(1);
    }

    @Test
    public void 확약조회테스트(){
        bookingMapper.deleteAllBooking();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startAt = LocalDate.parse("2022-12-11", formatter);
        LocalDate endAt =  LocalDate.parse("2022-12-16", formatter);
        Booking booking = new Booking("2465994341", 1, 4, startAt, endAt);
        bookingMapper.addBooking(booking);
        List<Booking> bookingList = bookingMapper.getBookingList(booking.getMemberId());
        Booking foundBooking = bookingMapper.getBookingInfo(bookingList.get(0).getBookingId(), bookingList.get(0).getMemberId());
        assertThat(foundBooking.getMemberId()).isEqualTo(booking.getMemberId());
    }

    @Test
    public void 전체확약조회테스트() {
        bookingMapper.deleteAllBooking();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startAt = LocalDate.parse("2022-12-11", formatter);
        LocalDate endAt =  LocalDate.parse("2022-12-16", formatter);
        int num = 6;
        for (int i = 1; i < num; i++) {
            Booking reservation = new Booking("2465994341", 1, 4, startAt, endAt);
            bookingMapper.addBooking(reservation);
            startAt = endAt;
            endAt = startAt.plusDays(5L);
        }
        List<Booking> reservationList = bookingMapper.getBookingList("2465994341");
        assertThat(reservationList.size()).isEqualTo(num - 1);
    }

    @Test
    public void 확약변경테스트(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startAt = LocalDate.parse("2022-12-11", formatter);
        LocalDate endAt =  LocalDate.parse("2022-12-16", formatter);
        Booking booking = new Booking("2465994341", 1, 4, startAt, endAt);
        bookingMapper.addBooking(booking);
        List<Booking> bookingList = bookingMapper.getBookingList(booking.getMemberId());

        startAt = endAt;
        endAt =  startAt.plusDays(5L);
        Booking modifiedBooking = Booking.builder()
                .bookingId(bookingList.get(0).getBookingId())
                .memberId(booking.getMemberId())
                .roomId(booking.getRoomId())
                .reservationId(booking.getReservationId())
                .startAt(startAt)
                .endAt(endAt)
                .build();
        bookingMapper.updateBooking(modifiedBooking);
        Booking foundBooking = bookingMapper.getBookingInfo(bookingList.get(0).getBookingId(), bookingList.get(0).getMemberId());
        assertThat(foundBooking.getRoomId()).isEqualTo(booking.getRoomId());
        assertThat(foundBooking.getStartAt()).isNotEqualTo(booking.getStartAt());
    }

    @Test
    public void 확약취소테스트() {
        bookingMapper.deleteAllBooking();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startAt = LocalDate.parse("2022-12-11", formatter);
        LocalDate endAt =  LocalDate.parse("2022-12-16", formatter);
        Booking booking = new Booking("2465994341", 1, 4, startAt, endAt);
        bookingMapper.addBooking(booking);
        List<Booking> bookingList = bookingMapper.getBookingList(booking.getMemberId());
        bookingMapper.deleteBooking(bookingList.get(0).getBookingId(), booking.getMemberId());
        assertThat(bookingMapper.getBookingList(booking.getMemberId()).size()).isEqualTo(0);
    }

}