package com.example.companyframe.service.impl;

import com.example.companyframe.entity.SysPermission;
import com.example.companyframe.mapper.SysPermissionMapper;
import com.example.companyframe.service.PermissionService;
import com.example.companyframe.vo.respVo.PermissionRespNodeVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Override
    public List<PermissionRespNodeVO> selectAllMenuByTree() {
        List<SysPermission> sysPermissions = sysPermissionMapper.selectAll();
        List<PermissionRespNodeVO> result = new ArrayList<>();
        PermissionRespNodeVO respNode = new PermissionRespNodeVO();
        respNode.setId("0");
        respNode.setTitle("默认顶级菜单");
        respNode.setSpread(true);
        respNode.setChildren(getTree(sysPermissions));
        result.add(respNode);
        return result;
    }

    /**
     * 递归获取菜单树
     *
     * @param all
     * @return
     */
    private List<PermissionRespNodeVO> getTree(List<SysPermission> all) {
        List<PermissionRespNodeVO> list = new ArrayList<>();
        if (all.isEmpty()) {
            return list;
        }
        for (SysPermission sysPermission : all) {
            if (sysPermission.getPid().equals("0")) {
                PermissionRespNodeVO respNodeVO = new PermissionRespNodeVO();
                BeanUtils.copyProperties(sysPermission, respNodeVO);
                respNodeVO.setTitle(sysPermission.getName());
                respNodeVO.setChildren(getChilder(sysPermission.getId(), all));
                list.add(respNodeVO);
            }
        }
        return list;
    }

    /**
     * 只递归获取目录和菜单
     *
     * @param id
     * @param all
     * @return
     */
    private List<PermissionRespNodeVO> getChilder(String id, List<SysPermission> all) {
        List<PermissionRespNodeVO> list = new ArrayList<>();
        for (SysPermission sysPermission : all) {
            if (sysPermission.getPid().equals(id) && sysPermission.getType() != 3) {
                PermissionRespNodeVO respNodeVO = new PermissionRespNodeVO();
                BeanUtils.copyProperties(sysPermission, respNodeVO);
                respNodeVO.setTitle(sysPermission.getName());
                respNodeVO.setChildren(getChilder(sysPermission.getId(), all));
                list.add(respNodeVO);
            }
        }
        return list;
    }

}
