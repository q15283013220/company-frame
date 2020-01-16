package com.example.companyframe.service.impl;

import com.example.companyframe.entity.SysPermission;
import com.example.companyframe.exception.BusinessException;
import com.example.companyframe.exception.code.BaseResponseCode;
import com.example.companyframe.mapper.SysPermissionMapper;
import com.example.companyframe.service.PermissionService;
import com.example.companyframe.vo.reqVo.PermissionAddReqVO;
import com.example.companyframe.vo.respVo.PermissionRespNodeVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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

    @Override
    public SysPermission addPermission(PermissionAddReqVO vo) {
        SysPermission sysPermission = new SysPermission();
        BeanUtils.copyProperties(vo, sysPermission);
        verifyForm(sysPermission);
        sysPermission.setId(UUID.randomUUID().toString());
        sysPermission.setCreateTime(new Date());
        int insert = sysPermissionMapper.insertSelective(sysPermission);
        if (insert != 1) {
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }
        return sysPermission;
    }


    /**
     * 操作后的菜单类型是目录的时候 父级必须为目录
     * 操作后的菜单类型是菜单的时候，父类必须为目录类型
     * 操作后的菜单类型是按钮的时候 父类必须为菜单类型
     */
    private void verifyForm(SysPermission sysPermission) {
        SysPermission parent = sysPermissionMapper.selectByPrimaryKey(sysPermission.getPid());
        switch (sysPermission.getType()) {
            case 1:
                if (parent != null) {
                    if (parent.getType() != 1) {
                        throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_CATALOG_ERROR);
                    }
                } else if (!sysPermission.getPid().equals("0")) {
                    throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_CATALOG_ERROR);
                }
                break;
            case 2:
                if (parent == null || parent.getType() != 1) {
                    throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_MENU_ERROR);
                }
                if (StringUtils.isEmpty(sysPermission.getUrl())) {
                    throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_URL_NOT_NULL);
                }
                break;
            case 3:
                if (parent == null || parent.getType() != 2) {
                    throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_BTN_ERROR);
                }
                if (StringUtils.isEmpty(sysPermission.getPerms())) {
                    throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_URL_PERMS_NULL);
                }
                if (StringUtils.isEmpty(sysPermission.getUrl())) {
                    throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_URL_NOT_NULL);
                }
                if (StringUtils.isEmpty(sysPermission.getMethod())) {
                    throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_URL_METHOD_NULL);
                }
                if (StringUtils.isEmpty(sysPermission.getCode())) {
                    throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_URL_CODE_NULL);
                }
                break;
        }
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

    @Override
    public List<PermissionRespNodeVO> permissionTreeList(String userId) {
        return getTree(selectAll());
    }
}
