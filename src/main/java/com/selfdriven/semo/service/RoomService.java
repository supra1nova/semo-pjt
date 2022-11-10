package com.selfdriven.semo.service;

import com.selfdriven.semo.entity.Room;
import com.selfdriven.semo.dto.login.Login;
import com.selfdriven.semo.repository.RoomImageMapper;
import com.selfdriven.semo.repository.RoomMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Transactional
public class RoomService {
    private final RoomMapper roomMapper;
    private final ProductService productService;
    private final RoomImageMapper roomImageMapper;

    public int addRoom(Room room, Login login) {
        int result = 0;
        try {
            if(productService.checkProduct(room.getProductId(), login.getId()) == false) {
                throw new Exception("업체 또는 유저 정보가 유효하지 않습니다. 다시 한 번 확인해주세요.");
            }
            result = roomMapper.addRoom(room);
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("e.getMessage() = " + e.getMessage());
        }
        return result;
    }

//    TODO: (확인필요) 특정 service 에서 관련되지 않은 다른 mapper를 호출해서 사용해도 좋은지?
    public Map<String, Object> getRoomById(Map<String, Integer> map) {
        Map<String, Object> result = new LinkedHashMap<>();
        try {
            Integer productId = map.get("productId");
            Integer roomId = map.get("roomId");
            if(productId == null) {
                throw new Exception("올바르지 않은 형태의 값이 업체 정보에 입력되었습니다. 다시 한 번 확인해주세요.");
            } else if (roomId == null) {
                throw new Exception("올바르지 않은 형태의 값이 객실 정보에 입력되었습니다. 다시 한 번 확인해주세요.");
            }
            Room room = roomMapper.getRoomById(productId, roomId);
            if(room == null) {
                throw new Exception("업체 또는 객실 정보가 유효하지 않습니다. 다시 한 번 확인해주세요.");
            }
            result.put("roomInfo", room);
            result.put("imageList", roomImageMapper.getRoomImagesById(roomId));
//            result.put("imageList", roomImageService.getAllRoomImagesByProductIdRoomId(productId, roomId));
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("e.getMessage() = " + e.getMessage());
            result = null;
        }
        return result;
    }

    public List<Room> getRoomList(int productId) {
        List<Room> result = null;
        try {
            result = roomMapper.getAllRoom(productId);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public int updateRoom(Room room, Login login) {
        int result = 0;
        try {
            if(!productService.checkProduct(room.getProductId(), login.getId())){
                throw new Exception("업체 또는 유저 정보가 유효하지 않습니다. 다시 한 번 확인해주세요.");
            } else if(!checkRoom(room.getProductId(), room.getRoomId())) {
                throw new Exception("업체 또는 객실 정보가 유효하지 않습니다. 다시 한 번 확인해주세요.");
            }
            result = roomMapper.updateRoom(room);
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("e.getMessage() = " + e.getMessage());
        }
        return result;
    }

    public int deleteRoom(Map<String, Integer> map, Login login){
        int result = 0;
        try {
            Integer productId = map.get("productId");
            Integer roomId = map.get("roomId");
            if(productId == null || roomId == null) {
                throw new Exception("올바르지 않은 형태의 값이 업체 또는 객실 정보에 입력되었습니다. 다시 한 번 확인해주세요.");
            } else if(!productService.checkProduct(productId, login.getId())) {
                throw new Exception("업체 또는 유저 정보가 유효하지 않습니다. 다시 한 번 확인해주세요.");
            } else if(!checkRoom(productId, roomId)) {
                throw new Exception("업체 또는 객실 정보가 유효하지 않습니다. 다시 한 번 확인해주세요.");
            }
            result = roomMapper.deleteRoom(roomId);
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("e.getMessage() = " + e.getMessage());
        }
        return result;
    }

    public Boolean checkRoom(int productId, int roomId) {
        return roomMapper.countValidRoom(productId, roomId) == 1 ? true : false;
    }

    public Integer getProductId(int roomId){
        return roomMapper.getProductId(roomId);
    }

    public int getRoomPrice(int roomId, LocalDate referenceDate){
        int daySequence = referenceDate.getDayOfWeek().getValue();
        return roomMapper.getRoomPriceByDate(roomId, daySequence);
    }

}
