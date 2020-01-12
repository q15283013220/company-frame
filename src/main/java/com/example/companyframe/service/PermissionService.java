package com.example.companyframe.service;

import com.example.companyframe.entity.SysPermission;

import java.util.List;

public interface PermissionService {
    List<SysPermission> selectAll();
}
