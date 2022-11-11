package com.selfdriven.semo.service;

import com.selfdriven.semo.entity.Reservation;
import com.selfdriven.semo.enums.ResultCode;
import com.selfdriven.semo.exception.ApiException;
import com.selfdriven.semo.repository.ReservationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@RequiredArgsConstructor
@Service
public class ReservationService {
    private final ReservationMapper reservationMapper;
    private final RoomService roomService;
    private final RentInfoService rentInfoService;

    public int insertReservation(Reservation reservation){
        Map<String, String> map = new HashMap<>();
        map.put("roomId", String.valueOf(reservation.getRoomId()));
        map.put("startAt", reservation.getStartAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        map.put("endAt", reservation.getEndAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        if(!getAvailability(map)){
            throw new ApiException(ResultCode.INVALID_DATE_ERROR);
        }
        return reservationMapper.addReservation(reservation);
    }

    public Reservation getReservationInfo(int reservationId, String memberId){
        return reservationMapper.getReservationInfo(reservationId, memberId);
    }

    public List<Reservation> getReservationList(String memberId){
        return reservationMapper.getReservationList(memberId);
    }

    // TODO: null 체크를 entity에서도 하고 있고, sql 내부에서 하고 있는데, 프론트에서도 null체크를 한번 하고 통과할 예정 -> sql에 있는 조건문을 빼도 되는지? 아니면 빼는게 맞는지?
    public int updateReservation(Reservation reservation, String memberId) {
        if(!reservation.getMemberId().equals(memberId)){
            throw new ApiException(ResultCode.ACCESS_DENIED);
        }
        Map<String, String> map = new HashMap<>();
        map.put("roomId", String.valueOf(reservation.getRoomId()));
        map.put("startAt", reservation.getStartAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        map.put("endAt", reservation.getEndAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        if(!getAvailability(map)){
            throw new ApiException(ResultCode.INVALID_DATE_ERROR);
        }
        if(!reservationMapper.getReservationInfo(reservation.getReservationId(), memberId).getStatus().equals("n")){
            throw new ApiException(ResultCode.DATE_ALREADY_BOOKED);
        }
        return reservationMapper.updateReservation(reservation);
    }

    public int deleteReservation(int reservationId, String memberId){
        if(getReservationInfo(reservationId, memberId).getStatus().equals("n")){
            throw new ApiException(ResultCode.DATE_ALREADY_BOOKED);
        }
        return reservationMapper.deleteReservation(reservationId, memberId);
    }


    public Boolean getAvailability(Map<String, String> map) {
        int roomId = Integer.valueOf(map.get("roomId"));
        // 예약 시작일을 가져온다.
        LocalDate startAt = LocalDate.parse(map.get("startAt"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        // 예약 마지막일을 가져온다.
        LocalDate endAt = LocalDate.parse(map.get("endAt"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        // 현재와 예약 시작일의 차이를 구한다.
        int gapFromToday = (int) ChronoUnit.DAYS.between(LocalDate.now(), startAt);
        // 만약 차이가 0보다 작으면 예약 불가
        if(gapFromToday < 0) {
            throw new ApiException(ResultCode.INVALID_START_DATE_ERROR);
        }
        // 예약 시작일과 마지막일의 차이를 구한다.
        int gapFromStartAt = (int) ChronoUnit.DAYS.between(startAt, endAt);
        // 만약 차이가 0보다 작거나 같으면 예약 불가
        if(gapFromStartAt <= 0){
            throw new ApiException(ResultCode.INVALID_FINISH_DATE_ERROR);
        }
        // 예약 기간에 해당하는 날짜를 모두를 가져온다.
        List<LocalDate> dateList = new ArrayList<>();
        while(!startAt.equals(endAt)){
            dateList.add(startAt);
            startAt = startAt.plusDays(1L);
        }
        // 각 날짜가 유효한 예약일인지 확인한다.
        Iterator dateIterator = dateList.iterator();
        while(dateIterator.hasNext()){
            LocalDate referenceDate = (LocalDate) dateIterator.next();
            int inavailableDate = reservationMapper.getInavailableDate(roomId, referenceDate);
            if(inavailableDate > 0) {
                throw new ApiException(ResultCode.INVALID_DATE_ERROR);
            }
        }
        return true;
    }

    public int getTotalPrice(Map<String, String> map) {
        int result = 0;
        int roomId = Integer.valueOf(map.get("roomId"));
        LocalDate startAt = LocalDate.parse(map.get("startAt"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate endAt = LocalDate.parse(map.get("endAt"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        try{
            List<LocalDate> dateList = new ArrayList<>();
            while(!startAt.equals(endAt)){
                dateList.add(startAt);
                startAt = startAt.plusDays(1L);
            }
            Iterator dateIterator = dateList.iterator();
            while(dateIterator.hasNext()){
                LocalDate referenceDate = (LocalDate) dateIterator.next();
                int rentInfoPrice = rentInfoService.getRentInfoPriceByDate(roomId, referenceDate);
                if(rentInfoPrice > 0) {
                    result += rentInfoPrice;
                } else {
                    result += roomService.getRoomPrice(roomId, referenceDate);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
