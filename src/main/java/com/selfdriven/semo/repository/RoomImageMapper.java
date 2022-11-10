package com.selfdriven.semo.repository;

import com.selfdriven.semo.entity.RoomImage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface RoomImageMapper {
    Integer insertRoomImage(RoomImage roomImage);
    List<RoomImage> getRoomImagesById(int roomId);
    Integer deleteRoomImage(String fileName);
    Integer countValidRoomImage(@Param("roomId") int roomId, @Param("imageUrl") String fileName);
    List<String> getAllImageUris(@Param("productId") int productId, @Param("roomId") int roomId);
}
