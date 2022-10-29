package com.selfdriven.semo.repository;

import com.selfdriven.semo.dto.RoomImage;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface RoomImageMapper {
    int insertRoomImage(RoomImage roomImage);
}
