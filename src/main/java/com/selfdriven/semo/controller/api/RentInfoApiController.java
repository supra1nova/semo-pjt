package com.selfdriven.semo.controller.api;

import com.selfdriven.semo.dto.ApiResponse;
import com.selfdriven.semo.dto.login.Login;
import com.selfdriven.semo.entity.RentInfo;
import com.selfdriven.semo.service.RentInfoService;
import com.selfdriven.semo.util.SessionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/rent-info")
public class RentInfoApiController {

    private final RentInfoService rentInfoService;

    @PostMapping("/create")
    public ApiResponse createRentInfo(@Valid @RequestBody RentInfo rentInfo, HttpSession session){
        Login login = SessionUtil.getLoginFromSession(session);
        int result = rentInfoService.insertRentInfo(rentInfo, login);
        return ApiResponse.ok(result);
    }

    @PostMapping("/load-one")
    public ApiResponse loadRentInfo(int rentId){
        RentInfo result = rentInfoService.getRentInfo(rentId);
        return ApiResponse.ok(result);
    }

    @PostMapping("/update")
    public ApiResponse updateRentInfo(@Valid @RequestBody RentInfo rentInfo, HttpSession session){
        Login login = SessionUtil.getLoginFromSession(session);
        int result = rentInfoService.updateRentInfo(rentInfo, login);
        return ApiResponse.ok(result);
    }

    @PostMapping("/delete")
    public ApiResponse deleteRentInfo(@RequestBody Map<String, Integer> map, HttpSession session){
        Login login = SessionUtil.getLoginFromSession(session);
        int result = rentInfoService.deleteRentInfo(map, login);
        return ApiResponse.ok(result);
    }
}
