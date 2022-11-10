package com.selfdriven.semo.repository;

import com.selfdriven.semo.entity.RentInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Mapper
@Repository
public interface RentInfoMapper {
    Integer addRentInfo(RentInfo rentInfo);
    RentInfo getRentInfo(int rentId);
    Integer updateRentInfo(RentInfo rentInfo);
    Integer deleteRentInfo(int rentId);
    Integer getRentInfoPriceByDate(@Param("roomId") int roomId, @Param("referenceDate") LocalDate referenceDate);
}
