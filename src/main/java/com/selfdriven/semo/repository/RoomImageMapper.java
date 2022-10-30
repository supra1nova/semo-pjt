package com.selfdriven.semo.repository;

import com.selfdriven.semo.dto.Room;
import com.selfdriven.semo.dto.RoomImage;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface RoomImageMapper {
    int insertRoomImage(RoomImage roomImage);
//    String selectRoomImage
    int deleteRoomImage(String fileName);
    int getRoomImageValidation(RoomImage roomImage);
    List<String> getAllImageUris(Room room);
}
