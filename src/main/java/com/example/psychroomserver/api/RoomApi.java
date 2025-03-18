package com.example.psychroomserver.api;

import com.example.psychroomserver.model.Room;
import com.example.psychroomserver.model.search.RoomSearchModel;
import com.example.psychroomserver.service.RoomService;
import com.example.psychroomserver.util.JsonResult;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/room",produces = MediaType.APPLICATION_JSON_VALUE)
public class RoomApi {
    RoomService roomService;

    @Autowired
    public void setRoomService(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public ResponseEntity<JsonResult> getRoom(@RequestParam(defaultValue = "1") Integer page,
                                              @RequestParam(defaultValue = "10") Integer limit,
                                              RoomSearchModel roomSearchModel) {
        Page<?> p = new Page<>(page, limit);
        List<Room> roomList = roomService.getAllRooms(roomSearchModel, p);
        PageInfo pi = new PageInfo(roomList);
        if (!roomList.isEmpty()) {
            JsonResult jr = JsonResult.success("查询成功", roomList);
            jr.getPageInfo().setTotal(pi.getTotal());
            jr.getPageInfo().setPageNum(p.getPageNum());
            jr.getPageInfo().setPageSize(p.getPageSize());
            return ResponseEntity.ok(jr);
        } else {
            return ResponseEntity.ok(JsonResult.fail("查询失败"));
        }
    }

    @DeleteMapping
    public ResponseEntity<JsonResult> deleteRoom(@RequestBody Integer[] ids) {
        int count = roomService.deleteRoom(ids);
        if (count > 0) {
            return ResponseEntity.ok(JsonResult.success("成功删除" + count + "条数据", null));
        } else {
            return ResponseEntity.ok(JsonResult.fail("删除失败"));
        }
    }

    @PostMapping
    public ResponseEntity<JsonResult> addRoom(@RequestBody Room room) {
        boolean success = roomService.addRoom(room);
        if (success) {
            return ResponseEntity.ok(JsonResult.success("添加成功", null));
        } else {
            return ResponseEntity.ok(JsonResult.fail("删除失败"));
        }
    }

    @PutMapping
    public ResponseEntity<JsonResult> updateRoom(@RequestBody Room room) {
        boolean success = roomService.updateRoom(room);
        if (success) {
            return ResponseEntity.ok(JsonResult.success("修改成功", null));
        } else {
            return ResponseEntity.ok(JsonResult.fail("修改失败"));
        }
    }
    @GetMapping("/list")
    public ResponseEntity<JsonResult> getRooms() {
        return ResponseEntity.ok(JsonResult.success("sc",roomService.getRoomList()));
    }
}
