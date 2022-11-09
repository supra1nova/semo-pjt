package com.selfdriven.semo.repository;

import com.selfdriven.semo.entity.RentInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface RentInfoMapper {
    Integer addRentInfo(RentInfo rentInfo);
    List<RentInfo> getRentInfo(int roomId);
    Integer updateRentInfo(RentInfo rentInfo);
    Integer deleteRentInfo(int rentId);
}
