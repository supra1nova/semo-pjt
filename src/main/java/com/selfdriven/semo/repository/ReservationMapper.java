package com.selfdriven.semo.repository;

import com.selfdriven.semo.entity.Reservation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Mapper
@Repository
public interface ReservationMapper {
    Integer addReservation(Reservation reservation);
    Reservation getReservationInfo(@Param("reservationId") int reservationId, @Param("memberId") String memberId);
    List<Reservation> getReservationList(String memberId);
    Integer updateReservation(Reservation reservation);
    Integer deleteReservation(@Param("reservationId") int reservationId, @Param("memberId") String memberId);
    Integer deleteAllReservation();
    Integer getInavailableDate(@Param("roomId") int roomId, @Param("referenceDate") LocalDate referenceDate);
}
