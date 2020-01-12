package com.example.companyframe.service.impl;

import com.example.companyframe.entity.SysPermission;
import com.example.companyframe.mapper.SysPermissionMapper;
import com.example.companyframe.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    @Override
    public List<SysPermission> selectAll() {
        List<SysPermission> permissions = sysPermissionMapper.selectAll();
        if (permissions != null) {
            for (SysPermission sysPermission : permissions) {
                SysPermission parent = sysPermissionMapper.selectByPrimaryKey(sysPermission.getPid());
                if (parent != null) {
                    sysPermission.setPidName(parent.getName());
                }
            }
        }
        return permissions;
    }
}
