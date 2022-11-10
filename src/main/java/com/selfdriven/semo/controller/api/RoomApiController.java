package com.selfdriven.semo.controller.api;

import com.selfdriven.semo.dto.ApiResponse;
import com.selfdriven.semo.entity.Room;
import com.selfdriven.semo.dto.login.Login;
import com.selfdriven.semo.service.RoomService;
import com.selfdriven.semo.util.SessionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/room")
public class RoomApiController {

    private final RoomService roomService;

    @PostMapping("/create")
    public ApiResponse createRoom(@Valid @RequestBody Room room, HttpSession session) {
        Login login = SessionUtil.getLoginFromSession(session);
        int result = roomService.addRoom(room, login);
        return result != 0 ? ApiResponse.ok(result) : ApiResponse.fail(1002, "객실을 생성할 수 없습니다.");
    }

    // TODO: (확인 필요) DTO를 이용해 validation 체크를 하는 것이 물론 낫지만, dto로 정의되지 않은 collection 객체들의 validation은 어떻게 해야할까? primitive 의 경우 그냥 정규표현식 사용 가능.
    @PostMapping("/info")
    public ApiResponse getRoomById(@RequestBody Map<String, Integer> map) {
        Map<String, Object> result = roomService.getRoomById(map);
        ApiResponse response = result != null ? ApiResponse.ok(result) : ApiResponse.fail(1002, "빈객체가 반환되었습니다.");
        return response;
    }

    // TODO: 이미지 포함해서 가져올수 있도록 수정 필요?
    @PostMapping("/list")
    public ApiResponse getRoomList(@RequestParam int productId) {
        List<Room> result = roomService.getRoomList(productId);
        ApiResponse response = result.size() != 0 ? ApiResponse.ok(result) : ApiResponse.fail(1002, "빈객체가 반환되었습니다.");
        return response;
    }

    @PostMapping("/edit")
    public ApiResponse editRoom(@Valid @RequestBody Room room, HttpSession session) {
        Login login = SessionUtil.getLoginFromSession(session);
        int result = roomService.updateRoom(room, login);
        return result != 0 ? ApiResponse.ok(result) : ApiResponse.fail(1002, "빈객체가 반환되었습니다.");
    }

    @PostMapping("/delete")
    public ApiResponse deleteRoom(@RequestBody Map<String, Integer> map, HttpSession session) {
        Login login = SessionUtil.getLoginFromSession(session);
        int result = roomService.deleteRoom(map, login);
        return result != 0 ? ApiResponse.ok(result) : ApiResponse.fail(1002, "빈객체가 반환되었습니다.");
    }
}
