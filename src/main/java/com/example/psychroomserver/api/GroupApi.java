package com.example.psychroomserver.api;

import com.example.psychroomserver.DTO.GroupRoleDTO;
import com.example.psychroomserver.model.chown.Group;
import com.example.psychroomserver.model.search.GroupSearchModel;
import com.example.psychroomserver.service.RoleService;
import com.example.psychroomserver.service.GroupService;
import com.example.psychroomserver.util.JsonResult;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/group")
public class GroupApi {
    GroupService groupService;
    RoleService roleService;

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setGroupService(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    public ResponseEntity<JsonResult> getGroups(@RequestParam(defaultValue = "1") Integer page,
                                               @RequestParam(defaultValue = "10") Integer limit,
                                               GroupSearchModel groupSearchModel) {
        Page<?> p = new Page<>(page, limit);
        List<Group> groupList = groupService.findAllGroups(groupSearchModel, p);
        PageInfo pi = new PageInfo(groupList);
        if (!groupList.isEmpty()) {
            JsonResult jr = JsonResult.success("查询成功", groupList);
            jr.getPageInfo().setTotal(pi.getTotal());
            jr.getPageInfo().setPageNum(p.getPageNum());
            jr.getPageInfo().setPageSize(p.getPageSize());
            return ResponseEntity.ok(jr);
        } else {
            return ResponseEntity.ok(JsonResult.fail("查询失败"));
        }
    }

    @DeleteMapping
    public ResponseEntity<JsonResult> deleteGroups(@RequestBody Integer[] ids) {

        int count = groupService.deleteGroupByIds(ids);
        if (count > 0) {
            return ResponseEntity.ok(JsonResult.success("成功删除" + count + "条数据", null));
        } else {
            return ResponseEntity.ok(JsonResult.fail("删除失败"));
        }
    }

    @PostMapping
    public ResponseEntity<JsonResult> addGroup(@RequestBody Group group) {
        boolean success = groupService.addGroup(group);
        if (success) {
            return ResponseEntity.ok(JsonResult.success("添加成功", null));
        } else {
            return ResponseEntity.ok(JsonResult.fail("删除失败"));
        }
    }

    /**
     * 列表使用
     * @return
     */
    @GetMapping("/role")
    public ResponseEntity<JsonResult> getRole() {
        return ResponseEntity.ok(JsonResult.success("查询成功", roleService.getAllRoles()));
    }

    @PutMapping
    public ResponseEntity<JsonResult> updateGroup(@RequestBody GroupRoleDTO rp) {
        //1.先删除数据库中间表中跟当前权限有关的所有数据
        //2.再添加传来的所有id
        groupService.renewRoles4Group(rp.getGroup().getId(),rp.getSelectedRoles());
        return ResponseEntity.ok(JsonResult.success("成功改变权限", null));
    }
}
