package com.selfdriven.semo.controller.api;

import com.selfdriven.semo.dto.ApiResponse;
import com.selfdriven.semo.dto.Room;
import com.selfdriven.semo.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/room")
public class RoomApiController {

    private final RoomService roomService;

    @PostMapping("/create")
    public ApiResponse createRoom(@Valid @RequestBody Room room) {
        int result = roomService.insertRoom(room);
        ApiResponse response = result != 0 ? ApiResponse.ok(result) : ApiResponse.fail(1002, "빈객체가 반환되었습니다.");
        return response;
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
    public ApiResponse deleteRoom(@RequestParam String roomId) {
        int result = roomService.deleteRoom(roomId);
        ApiResponse response = result != 0 ? ApiResponse.ok(result) : ApiResponse.fail(1002, "빈객체가 반환되었습니다.");
        return response;
    }

    @PostMapping("/edit")
    public ApiResponse editRoom(Room room) {
        int result = roomService.updateRoom(room);
        ApiResponse response = result != 0 ? ApiResponse.ok(result) : ApiResponse.fail(1002, "빈객체가 반환되었습니다.");
        return response;
    }
}
