package com.selfdriven.semo.repository;

import com.selfdriven.semo.entity.Room;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface RoomMapper {
    Integer addRoom(Room room);
    Room getRoomById(@Param("productId") int productId, @Param("roomId") int roomId);
    List<Room> getAllRoom(int productId);
    Integer updateRoom(Room room);
    Integer deleteRoom(int roomId);
    Integer countValidRoom(@Param("productId") int productId, @Param("roomId") int roomId);
    Integer getProductId(int roomId);
}

