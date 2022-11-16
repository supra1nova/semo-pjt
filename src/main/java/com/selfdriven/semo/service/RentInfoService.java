package com.selfdriven.semo.service;

import com.selfdriven.semo.dto.login.Login;
import com.selfdriven.semo.entity.RentInfo;
import com.selfdriven.semo.enums.ResultCode;
import com.selfdriven.semo.exception.ApiException;
import com.selfdriven.semo.repository.RentInfoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class RentInfoService {

    private final RoomService roomService;
    private final ProductService productService;
    private final RentInfoMapper rentInfoMapper;

    public int insertRentInfo(RentInfo rentInfo, Login login){
        int result = 0;
        // 객실, 업체, 유저 정보 유효성 평가 진행
        checkRentInfo(rentInfo, login.getId());
        LocalDate startAt = rentInfo.getStartAt();
        LocalDate endAt = rentInfo.getEndAt();
        LocalDate now = LocalDate.now();
        // 현재 날짜 이후 시점부터 시작일 선택 가능
        if(now.until(startAt, ChronoUnit.DAYS) < 0) {
            throw new ApiException(ResultCode.INVALID_START_DATE_ERROR);
        }
        // 시작일보다 최소 1일 이후 시점부터 마감 시간 선택 가능
        if(startAt.until(endAt, ChronoUnit.DAYS) <= 0) {
            throw new ApiException(ResultCode.INVALID_FINISH_DATE_ERROR);
        }
        // end. 등록한다.
        result = rentInfoMapper.addRentInfo(rentInfo);
        return result ;
    }

    public RentInfo getRentInfo(int rentId){
        RentInfo result = rentInfoMapper.getRentInfo(rentId);
        return result;
    }

//    // TODO: update 로직은 어떻게 가야할지? 단순히 시간 조정이 가능하도록? 아니면 생성과 동일한 로직?(시작 시간 > 현재 시간, 마감시간 > 시작시간)
    public int updateRentInfo(RentInfo rentInfo, Login login) {
        checkRentInfo(rentInfo, login.getId());
        LocalDate startAt = rentInfo.getStartAt();
        LocalDate endAt = rentInfo.getEndAt();
        LocalDate now = LocalDate.now();
        if(now.until(startAt, ChronoUnit.DAYS) < 0) {
            throw new ApiException(ResultCode.INVALID_START_DATE_ERROR);
        }
        if(startAt.until(endAt, ChronoUnit.DAYS) < 0) {
            throw new ApiException(ResultCode.INVALID_FINISH_DATE_ERROR);
        }
        int result = rentInfoMapper.updateRentInfo(rentInfo);
        return result;
    }

    public int deleteRentInfo(@RequestBody Map<String, Integer> map, Login login){
        Integer roomId = map.get("roomId");
        Integer rentId = map.get("rentId");
        if(roomId == null || rentId == null) {
            throw new ApiException(ResultCode.INVALID_PARAMETER);
        }
        checkRentInfo(RentInfo.builder().roomId(roomId).rentId(rentId).build(), login.getId());
        int result = rentInfoMapper.deleteRentInfo(rentId);
        return result;
    }

    private void checkRentInfo(RentInfo rentInfo, String memberId) {
        Integer productId = roomService.getProductId(rentInfo.getRoomId());
        if(productId == null || productId == 0 || !productService.checkProduct(productId, memberId)) {
            throw new ApiException(ResultCode.INVALID_PARAMETER);
        }
    }

    // 특정날짜에 별도의 가격 설정이 되어 있는지 확인하고 있으면 가격을 리턴, 없으면 0을 return
    public int getRentInfoPriceByDate(int roomId, LocalDate referenceDate) {
        int result = 0;
        Integer res = rentInfoMapper.getRentInfoPriceByDate(roomId, referenceDate);
        if(res != null){
            result = res;
        }
        return result;
    }
}
