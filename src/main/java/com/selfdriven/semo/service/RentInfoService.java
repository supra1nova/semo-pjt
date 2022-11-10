package com.selfdriven.semo.service;

import com.selfdriven.semo.dto.login.Login;
import com.selfdriven.semo.entity.RentInfo;
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
        try {
            // 객실, 업체, 유저 정보 유효성 평가 진행
            checkRentInfo(rentInfo, login.getId());
            LocalDate startAt = rentInfo.getStartAt();
            LocalDate endAt = rentInfo.getEndAt();
            LocalDate now = LocalDate.now();
            // 현재 날짜 이후 시점부터 시작일 선택 가능
            if(now.until(startAt, ChronoUnit.DAYS) < 0) {
                throw new Exception("현재보다 이전의 날짜를 시작일로 선택할 수 없습니다. 다시 한 번 확인해주세요.");
            }
            // 시작일보다 최소 1일 이후 시점부터 마감 시간 선택 가능
            if(startAt.until(endAt, ChronoUnit.DAYS) <= 0) {
                throw new Exception("예약 가능일을 시작일보다 과거 시점으로 지정할 수는 없습니다. 다시 한 번 확인해주세요.");
            }
            // end. 등록한다.
            result = rentInfoMapper.addRentInfo(rentInfo);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return result ;
    }

    public RentInfo getRentInfo(int rentId){
        RentInfo result = null;
        try {
            result = rentInfoMapper.getRentInfo(rentId);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }

//    // TODO: update 로직은 어떻게 가야할지? 단순히 시간 조정이 가능하도록? 아니면 생성과 동일한 로직?(시작 시간 > 현재 시간, 마감시간 > 시작시간)
    public int updateRentInfo(RentInfo rentInfo, Login login) {
        int result = 0;
        try {
            checkRentInfo(rentInfo, login.getId());
            LocalDate startAt = rentInfo.getStartAt();
            LocalDate endAt = rentInfo.getEndAt();
            LocalDate now = LocalDate.now();
            if(now.until(startAt, ChronoUnit.DAYS) < 0) {
                throw new Exception("입실 시간은 현재 날짜로부터 최소 하루의 간격을 둬야 합니다. 다시 한 번 확인해주세요.");
            }
            if(startAt.until(endAt, ChronoUnit.DAYS) < 0) {
                throw new Exception("예약 가능일을 시작일보다 과거의 시점으로 지정할 수는 없습니다. 다시 한 번 확인해주세요.");
            }
            result = rentInfoMapper.updateRentInfo(rentInfo);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public int deleteRentInfo(@RequestBody Map<String, Integer> map, Login login){
        int result = 0;
        try {
            Integer roomId = map.get("roomId");
            Integer rentId = map.get("rentId");
            if(roomId == null || rentId == null) {
                throw new Exception("올바르지 않은 형태의 값이 업체 또는 객실 정보에 입력되었습니다. 다시 한 번 확인해주세요.");
            }
            checkRentInfo(RentInfo.builder().roomId(roomId).rentId(rentId).build(), login.getId());
            result = rentInfoMapper.deleteRentInfo(rentId);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private void checkRentInfo(RentInfo rentInfo, String memberId) throws Exception {
        Integer productId = roomService.getProductId(rentInfo.getRoomId());
        if(productId == null){
            throw new Exception("객실 정보가 유효하지 않습니다. 다시 한 번 확인해주세요.");
        }else if(productId == 0) {
            throw new Exception("업체 또는 객실 정보가 유효하지 않습니다. 다시 한 번 확인해주세요.");
        } else if(!productService.checkProduct(productId, memberId)) {
            throw new Exception("업체 또는 유저 정보가 유효하지 않습니다. 다시 한 번 확인해주세요.");
        }
    }

    public int getRentInfoPriceByDate(int roomId, LocalDate referenceDate) {
        int result = 0;
        try {
            Integer res = rentInfoMapper.getRentInfoPriceByDate(roomId, referenceDate);
            if(res != null){
                result = res;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
