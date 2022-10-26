package com.selfdriven.semo.controller.api;

import com.selfdriven.semo.dto.ApiResponse;
import com.selfdriven.semo.dto.Product;
import com.selfdriven.semo.dto.Room;
import com.selfdriven.semo.dto.login.Login;
import com.selfdriven.semo.enums.ResultCode;
import com.selfdriven.semo.service.MemberService;
import com.selfdriven.semo.service.ProductService;
import com.selfdriven.semo.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/room")
public class RoomApiController {

    private final RoomService roomService;
    private final MemberService memberService;
    private final ProductService productService;

    @PostMapping("/create")
    public ApiResponse createRoom(@Valid @RequestBody Room room, HttpSession session) {
        ApiResponse response = null;
        try {
            int result = 0;
            if(validationChecking(room, session)) result = roomService.insertRoom(room);
            return response = result != 0 ? ApiResponse.ok(result) : ApiResponse.fail(1002, "빈객체가 반환되었습니다.");
        } catch(Exception e) {
            e.printStackTrace();
            return response = ApiResponse.fail(ResultCode.ACCESS_DENIED.getCode(), ResultCode.ACCESS_DENIED.getMessage());
        }
    }

    @GetMapping("/list")
    public ApiResponse getRoomList() {
        List<Room> result = roomService.getRoomList();
        ApiResponse response = result.size() != 0 ? ApiResponse.ok(result) : ApiResponse.fail(1002, "빈객체가 반환되었습니다.");
        return response;
    }

    @GetMapping("/info")
    public ApiResponse getRoomByRoomId(String roomId) {
        Room result = roomService.getRoomByRoomId(roomId);
        ApiResponse response = result != null ? ApiResponse.ok(result) : ApiResponse.fail(1002, "빈객체가 반환되었습니다.");
        return response;
    }

    @PostMapping("/delete")
    public ApiResponse deleteRoom(@RequestParam String roomId, HttpSession session) {
        ApiResponse response = null;
        try {
            int result = 0;
            if(validationChecking(roomService.getRoomByRoomId(roomId), session)) result = roomService.deleteRoom(roomId);
            return response = result != 0 ? ApiResponse.ok(result) : ApiResponse.fail(1002, "빈객체가 반환되었습니다.");
        } catch(Exception e) {
            e.printStackTrace();
            return response = ApiResponse.fail(ResultCode.ACCESS_DENIED.getCode(), ResultCode.ACCESS_DENIED.getMessage());
        }
    }

    @PostMapping("/edit")
    public ApiResponse editRoom(@Valid @RequestBody Room room, HttpSession session) {
        ApiResponse response = null;
        try {
            int result = 0;
            if(validationChecking(room, session)) result = roomService.updateRoom(room);
            return response = result != 0 ? ApiResponse.ok(result) : ApiResponse.fail(1002, "빈객체가 반환되었습니다.");
        } catch(Exception e) {
            e.printStackTrace();
            return response = ApiResponse.fail(ResultCode.ACCESS_DENIED.getCode(), ResultCode.ACCESS_DENIED.getMessage());
        }
    }

    public Boolean validationChecking(Room room, HttpSession session){
        String memberId = ((Login)session.getAttribute("login")).getId();
        Boolean isMemberTypeValid = memberService.checkMemberType("c", memberId);
        Boolean isCustomerValid = productService.checkOwner(room.getProductId(), memberId);
        return isMemberTypeValid && isCustomerValid;
    }
}
