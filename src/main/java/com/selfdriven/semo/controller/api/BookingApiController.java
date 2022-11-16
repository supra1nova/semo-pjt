package com.selfdriven.semo.controller.api;

import com.selfdriven.semo.dto.ApiResponse;
import com.selfdriven.semo.dto.login.Login;
import com.selfdriven.semo.entity.Booking;
import com.selfdriven.semo.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/booking")
public class BookingApiController {

    private final BookingService bookingService;

    @PostMapping("/create")
    public ApiResponse createBooking(@Valid @RequestBody Booking booking){
        int result = bookingService.insertBooking(booking);
        return ApiResponse.ok(result);
    }

    @PostMapping("/load-one")
    public ApiResponse loadBookingInfo(int bookingId, HttpSession session){
        Login login = (Login)session.getAttribute("login");
        Booking result = bookingService.getBookingInfo(bookingId, login.getId());
        return ApiResponse.ok(result);
    }

    @PostMapping("/load-all")
    public ApiResponse loadBookingList(HttpSession session){
        Login login = (Login)session.getAttribute("login");
        List<Booking> result = bookingService.getBookingList(login.getId());
        return ApiResponse.ok(result);
    }

    @PostMapping("/update")
    public ApiResponse updateBookingInfo(@Valid @RequestBody Booking booking){
        int result = bookingService.updateBooking(booking);
        return ApiResponse.ok(result);
    }

    @PostMapping("/delete")
    public ApiResponse deleteBooking(int bookingId) {
        int result = bookingService.deleteBooking(bookingId);
        return ApiResponse.ok(result);
    }
}
