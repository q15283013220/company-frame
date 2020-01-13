package com.example.companyframe.controller;

import com.example.companyframe.entity.SysPermission;
import com.example.companyframe.service.PermissionService;
import com.example.companyframe.utils.DataResult;
import com.example.companyframe.vo.respVo.PermissionRespNodeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api")
@Api(tags = "组织管理-菜单权限管理")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    @GetMapping("/permissions")
    @ApiOperation("获取所有菜单权限数据")
    public DataResult getselectAll() {
        List<SysPermission> sysPermissions = permissionService.selectAll();
        return DataResult.success(sysPermissions);
    }

    @GetMapping("/permission/tree")
    @ApiOperation("菜单权限树接口-递归查询")
    public DataResult getAllPermissionTree() {
        List<PermissionRespNodeVO> list = permissionService.selectAllMenuByTree();
        return DataResult.success(list);
    }
}
