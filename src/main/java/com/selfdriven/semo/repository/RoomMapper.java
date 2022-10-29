package com.selfdriven.semo.repository;

import com.selfdriven.semo.dto.Room;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface RoomMapper {
    int insertRoom(Room room);
    List<Room> selectAllRoom();
    List<Room> selectAllRoomByProductId(int productId);
    Room selectRoomById(String roomId);
    int updateRoom(Room room);
    int deleteRoom(String roomId);
    int deleteAllRoom();
    int countAllRoom();
    int selectRoomValidation(int roomId);
}

