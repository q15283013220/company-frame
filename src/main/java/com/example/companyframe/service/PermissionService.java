package com.example.companyframe.service;

import com.example.companyframe.entity.SysPermission;
import com.example.companyframe.vo.respVo.PermissionRespNodeVO;

import java.util.List;

public interface PermissionService {
    List<SysPermission> selectAll();

    /**
     * 查询菜单权限
     */
    List<PermissionRespNodeVO> selectAllMenuByTree();
}
