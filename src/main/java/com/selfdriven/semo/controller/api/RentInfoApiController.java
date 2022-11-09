package com.selfdriven.semo.controller.api;

import com.selfdriven.semo.dto.ApiResponse;
import com.selfdriven.semo.dto.login.Login;
import com.selfdriven.semo.entity.RentInfo;
import com.selfdriven.semo.enums.ResultCode;
import com.selfdriven.semo.service.RentInfoService;
import com.selfdriven.semo.util.SessionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
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
        return result != 0 ? ApiResponse.ok(result) : ApiResponse.fail(ResultCode.UNKNOWN_ERROR.getCode(), ResultCode.UNKNOWN_ERROR.getMessage());
    }

    @PostMapping("/load-one")
    public ApiResponse loadRentInfo(@RequestParam int roomId){
        List<RentInfo> result = rentInfoService.getRentInfo(roomId);
        return result != null ? ApiResponse.ok(result) : ApiResponse.fail(ResultCode.UNKNOWN_ERROR.getCode(), ResultCode.UNKNOWN_ERROR.getMessage());
    }

    @PostMapping("/update")
    public ApiResponse updateRentInfo(@Valid @RequestBody RentInfo rentInfo, HttpSession session){
        Login login = SessionUtil.getLoginFromSession(session);
        int result = rentInfoService.updateRentInfo(rentInfo, login);
        return result != 0 ? ApiResponse.ok(result) : ApiResponse.fail(ResultCode.UNKNOWN_ERROR.getCode(), ResultCode.UNKNOWN_ERROR.getMessage());
    }

    @PostMapping("/delete")
    public ApiResponse deleteRentInfo(@RequestBody Map<String, Integer> map, HttpSession session){
        Login login = SessionUtil.getLoginFromSession(session);
        int result = rentInfoService.deleteRentInfo(map, login);
        return result != 0 ? ApiResponse.ok(result) : ApiResponse.fail(ResultCode.UNKNOWN_ERROR.getCode(), ResultCode.UNKNOWN_ERROR.getMessage());
    }
}
