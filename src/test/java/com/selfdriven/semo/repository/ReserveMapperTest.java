package com.selfdriven.semo.repository;

import com.selfdriven.semo.entity.Reservation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class ReserveMapperTest {

    @Autowired
    private ReservationMapper reservationMapper;

    @Test
    public void 예약생성테스트() {
        reservationMapper.deleteAllReservation();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startAt = LocalDate.parse("2022-12-11", formatter);
        LocalDate endAt =  LocalDate.parse("2022-12-16", formatter);
        Reservation reservation = new Reservation("2465994341", 4, "cash", 50000 * (int) ChronoUnit.DAYS.between(startAt,endAt), startAt, endAt);
        reservationMapper.addReservation(reservation);
        assertThat(reservationMapper.getReservationList(reservation.getMemberId()).size()).isEqualTo(1);
    }

    @Test
    public void 예약조회테스트(){
        reservationMapper.deleteAllReservation();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startAt = LocalDate.parse("2022-12-11", formatter);
        LocalDate endAt =  LocalDate.parse("2022-12-16", formatter);
        Reservation reservation = new Reservation("2465994341", 4, "cash", 50000 * (int) ChronoUnit.DAYS.between(startAt,endAt), startAt, endAt);
        reservationMapper.addReservation(reservation);
        List<Reservation> reservationList = reservationMapper.getReservationList(reservation.getMemberId());
        Reservation foundReservation = reservationMapper.getReservationInfo(reservationList.get(0).getReservationId(), reservationList.get(0).getMemberId());
        assertThat(foundReservation.getMemberId()).isEqualTo(reservation.getMemberId());
    }

    @Test
    public void 전체예약조회테스트() {
        reservationMapper.deleteAllReservation();
        int num = 6;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startAt = LocalDate.parse("2022-12-11", formatter);
        LocalDate endAt =  LocalDate.parse("2022-12-16", formatter);
        for (int i = 1; i < num; i++) {
//            Reservation reservation = new Reservation("2465994341", 4, "cash", 50000 * endAt.compareTo(startAt), endAt.compareTo(startAt), startAt, endAt);
//            Reservation reservation = new Reservation("2465994341", 4, "cash", 50000 * (int) ChronoUnit.DAYS.between(startAt,endAt), (int) ChronoUnit.DAYS.between(startAt,endAt), startAt, endAt);
            Reservation reservation = new Reservation("2465994341", 4, "cash", 50000 * (int) startAt.until(endAt, ChronoUnit.DAYS), startAt, endAt);
            reservationMapper.addReservation(reservation);
            startAt = endAt;
            endAt = startAt.plusDays(5L);
        }
        List<Reservation> reservationList = reservationMapper.getReservationList("2465994341");
        assertThat(reservationList.size()).isEqualTo(num - 1);
    }

    @Test
    public void 예약변경테스트(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startAt = LocalDate.parse("2022-12-11", formatter);
        LocalDate endAt =  LocalDate.parse("2022-12-16", formatter);
        Reservation reservation = new Reservation("2465994341", 4, "cash", 50000 * (int) ChronoUnit.DAYS.between(startAt,endAt), startAt, endAt);
        reservationMapper.addReservation(reservation);
        List<Reservation> reservationList = reservationMapper.getReservationList(reservation.getMemberId());

        startAt = endAt.plusDays(1L);
        endAt =  startAt.plusDays(4);
        Reservation modifiedReservation = Reservation.builder()
                .reservationId(reservationList.get(0).getReservationId())
                .memberId(reservation.getMemberId())
                .paymentTerm("card")
                .totalPrice(60000 * endAt.compareTo(startAt))
                .startAt(startAt)
                .endAt(endAt)
                .build();
        reservationMapper.updateReservation(modifiedReservation);
        Reservation foundReservation = reservationMapper.getReservationInfo(reservationList.get(0).getReservationId(), reservationList.get(0).getMemberId());
        assertThat(foundReservation.getMemberId()).isEqualTo(reservation.getMemberId());
        assertThat(foundReservation.getStartAt()).isNotEqualTo(reservation.getStartAt());
    }

    @Test
    public void 예약취소테스트() {
        reservationMapper.deleteAllReservation();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startAt = LocalDate.parse("2022-12-11", formatter);
        LocalDate endAt =  LocalDate.parse("2022-12-16", formatter);
        Reservation reservation = new Reservation("2465994341", 4, "cash", 50000 * (int) ChronoUnit.DAYS.between(startAt,endAt), startAt, endAt);
        reservationMapper.addReservation(reservation);
        List<Reservation> reservationList = reservationMapper.getReservationList(reservation.getMemberId());
        reservationMapper.deleteReservation(reservationList.get(0).getReservationId(), reservation.getMemberId());
        assertThat(reservationMapper.getReservationList(reservation.getMemberId()).size()).isEqualTo(0);
    }

    @Test
    public void 불가능날테스트(){
        reservationMapper.deleteAllReservation();
        int num = 6;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startAt = LocalDate.parse("2022-12-11", formatter);
        LocalDate endAt =  LocalDate.parse("2022-12-16", formatter);
        for (int i = 1; i < num; i++) {
            Reservation reservation = new Reservation("2465994341", 4, "cash", 50000 * (int) ChronoUnit.DAYS.between(startAt,endAt), startAt, endAt);
            startAt = endAt;
            endAt = startAt.plusDays(5L);
            reservationMapper.addReservation(reservation);
        }
        int count = reservationMapper.getInavailableDate(4, LocalDate.parse("2023-01-04", formatter));
        assertThat(count).isGreaterThan(0);

        List<LocalDate> dateList = new ArrayList<>();
        LocalDate date = LocalDate.parse("2022-12-07", formatter);
        int days = 5;
        for(int i = 0; i < days; i++){
            dateList.add(date);
            date = date.plusDays(1L);
        }
        List<Integer> result = new ArrayList<>();
        Iterator dateIterator = dateList.stream().iterator();
        while(dateIterator.hasNext()){
            LocalDate iteratorDate = (LocalDate) dateIterator.next();
            result.add(reservationMapper.getInavailableDate(4, iteratorDate));
        }
        assertEquals(true, result.contains(1));
    }
}