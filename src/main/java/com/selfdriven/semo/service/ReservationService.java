package com.selfdriven.semo.service;

import com.selfdriven.semo.entity.Reservation;
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
        int result = 0;
        try {
            Map<String, String> map = new HashMap<>();
            map.put("roomId", String.valueOf(reservation.getRoomId()));
            map.put("startAt", reservation.getStartAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            map.put("endAt", reservation.getEndAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            if(!getAvailability(map)){
                throw new Exception("해당 객실의 선택된 날짜는 이미 예약이 되었거나, 예약 진행중입니다. 다시 한 번 확인해주세요.");
            }
            result = reservationMapper.addReservation(reservation);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public Reservation getReservationInfo(int reservationId, String memberId){
        Reservation result = null;
        try {
            result = reservationMapper.getReservationInfo(reservationId, memberId);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Reservation> getReservationList(String memberId){
        List<Reservation> result = null;
        try {
            result = reservationMapper.getReservationList(memberId);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // TODO: null 체크를 entity에서도 하고 있고, sql 내부에서 하고 있는데, 프론트에서도 null체크를 한번 하고 통과할 예정 -> sql에 있는 조건문을 빼도 되는지? 아니면 빼는게 맞는지?
    public int updateReservation(Reservation reservation, String memberId) {
        int result = 0;
        try {
            if(!reservation.getMemberId().equals(memberId)){
                throw new Exception("예약 당사자가 아닙니다. 다시 한 번 확인해 주세요.");
            }
            Map<String, String> map = new HashMap<>();
            map.put("roomId", String.valueOf(reservation.getRoomId()));
            map.put("startAt", reservation.getStartAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            map.put("endAt", reservation.getEndAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            if(!getAvailability(map)){
                throw new Exception("해당 객실의 선택된 날짜는 이미 예약이 되었거나, 예약 진행중입니다. 다시 한 번 확인해주세요.");
            }
            if(!reservationMapper.getReservationInfo(reservation.getReservationId(), memberId).getStatus().equals("n")){
                throw new Exception("이미 확정된 예약입니다. 확정 예약 내역에서 취소해주세요.");
            }
            result = reservationMapper.updateReservation(reservation);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public int deleteReservation(int reservationId, String memberId){
        int result = 0;
        try {
            if(getReservationInfo(reservationId, memberId).getStatus().equals("n")){
                throw new Exception("확정된 예약입니다. 확정 예약 내역에서 취소해주세요.");
            }
            result = reservationMapper.deleteReservation(reservationId, memberId);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public Boolean getAvailability(Map<String, String> map) {
        Boolean result = false;
        int roomId = Integer.valueOf(map.get("roomId"));
        LocalDate startAt = LocalDate.parse(map.get("startAt"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate endAt = LocalDate.parse(map.get("endAt"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        try{
            int gapFromToday = (int) ChronoUnit.DAYS.between(LocalDate.now(), startAt);
            if(gapFromToday < 0) {
                throw new Exception("입실 날짜는 현재 날짜보다 과거일 수 없습니다. 다시 한 번 확인해주세요.");
            }
            int gapFromStartAt = (int) ChronoUnit.DAYS.between(startAt, endAt);
            if(gapFromStartAt <= 0){
                throw new Exception("퇴실 날짜는 입실 날짜로부터 최소 하루의 차이가 있어야합니다.");
            }
            int unavailableStartDateCount = reservationMapper.getInavailableStartDate(roomId, startAt);
            int unavailableEndDateCount = reservationMapper.getInavailableStartDate(roomId, endAt);
            if(unavailableStartDateCount != 0 || unavailableEndDateCount != 0){
                throw new Exception("해당 객실의 선택된 날짜는 이미 예약이 되었거나, 예약 진행중입니다. 다시 한 번 확인해주세요.");
            }
            result = true;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return result;
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
