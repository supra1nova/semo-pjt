package com.selfdriven.semo.controller.api;

import com.selfdriven.semo.dto.ApiResponse;
import com.selfdriven.semo.dto.login.Login;
import com.selfdriven.semo.entity.Reservation;
import com.selfdriven.semo.enums.ResultCode;
import com.selfdriven.semo.service.ReservationService;
import com.selfdriven.semo.util.SessionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reservation")
public class ReservationApiController {

    private final ReservationService reservationService;

    @PostMapping("/create")
    public ApiResponse createReservation(@Valid @RequestBody Reservation reservation){
        int result = reservationService.insertReservation(reservation);
        return ApiResponse.ok(result);
    }

    @PostMapping("/load-one")
    public ApiResponse loadReservationInfo(int reservationId, HttpSession session){
        Login login = (Login)session.getAttribute("login");
        Reservation result = reservationService.getReservationInfo(reservationId, login.getId());
        return ApiResponse.ok(result);
    }

    @PostMapping("/load-all")
    public ApiResponse loadReservationList(HttpSession session){
        Login login = (Login)session.getAttribute("login");
        List<Reservation> result = reservationService.getReservationList(login.getId());
        return ApiResponse.ok(result);
    }

    @PostMapping("/update")
    public ApiResponse updateReservationInfo(@Valid @RequestBody Reservation reservation, HttpSession session){
        Login login = (Login)session.getAttribute("login");
        int result = reservationService.updateReservation(reservation, login.getId());
        return ApiResponse.ok(result);
    }

    @PostMapping("/delete")
    public ApiResponse deleteReservation(int reservationId, HttpSession session) {
        Login login = SessionUtil.getLoginFromSession(session);
        int result = reservationService.deleteReservation(reservationId, login.getId());
        return ApiResponse.ok(result);
    }

    @PostMapping("/check-price")
    public ApiResponse checkReservationPrice(@RequestBody Map<String, String> map){
        int result = reservationService.getTotalPrice(map);
        return ApiResponse.ok(result);
    }

    @PostMapping("/check-available-date")
    public ApiResponse checkAvailableDate(@RequestBody Map<String, String> map){
        Boolean result = reservationService.getAvailability(map);
        return ApiResponse.ok(result);
    }
}
