package com.selfdriven.semo.service;

import com.selfdriven.semo.dto.Room;
import com.selfdriven.semo.repository.RoomMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class RoomService {
    private final RoomMapper roomMapper;

    public int insertRoom(Room room) {
        return roomMapper.insertRoom(room);
    }

    public List<Room> getRoomList() {
        return roomMapper.selectAllRoom();
    }

    public Room getRoomByRoomId(String roomId) {
        return roomMapper.selectRoomById(roomId);
    }

    public int updateRoom(Room room) {
        return roomMapper.updateRoom(room);
    }

    public int deleteRoom(String roomId){
        return roomMapper.deleteRoom(roomId);
    }

    public int deleteAllRoom(){
        return roomMapper.deleteAllRoom();
    }

    public int countAllRoom(){
        return roomMapper.countAllRoom();
    }

//    public Boolean checkRoomValidation(int roomId){
//        return roomMapper.selectRoomValidation(roomId) == 1 ? true : false;
//    }

    public Boolean checkRoomValidation(Room room){
        return roomMapper.selectRoomValidationNew(room) == 1 ? true : false;
    }

}
