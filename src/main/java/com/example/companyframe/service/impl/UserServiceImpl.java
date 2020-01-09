package com.example.companyframe.service.impl;

import com.example.companyframe.constants.Constant;
import com.example.companyframe.entity.SysUser;
import com.example.companyframe.exception.BusinessException;
import com.example.companyframe.exception.code.BaseResponseCode;
import com.example.companyframe.mapper.SysUserMapper;
import com.example.companyframe.service.RedisService;
import com.example.companyframe.service.UserService;
import com.example.companyframe.utils.JWTTokenUtil;
import com.example.companyframe.utils.PageUtils;
import com.example.companyframe.utils.PasswordUtils;
import com.example.companyframe.vo.reqVo.LoginReqVO;
import com.example.companyframe.vo.reqVo.UserPageReqVO;
import com.example.companyframe.vo.respVo.LoginRespVO;
import com.example.companyframe.vo.respVo.PageVO;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private RedisService redisService;

    @Override
    public void logout(String accessToken, String refreshToken) {
        if (StringUtils.isEmpty(accessToken) || StringUtils.isEmpty(refreshToken)) {
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }
        Subject subject = SecurityUtils.getSubject();
        log.info("subject.getPrincipals()={}", subject.getPrincipals());
        if (subject.isAuthenticated()) {
            subject.logout();
        }
        String userId = JWTTokenUtil.getUserId(accessToken);

        /*
         * 把token 加入黑名单 禁止再登录
         */
        redisService.set(Constant.JWT_REFRESH_TOKEN_BLACKLIST + accessToken, userId, JWTTokenUtil.getRemainingTime(accessToken), TimeUnit.MILLISECONDS);
        /*
         * 把 refreshToken 加入黑名单 禁止再拿来刷新token
         */
        redisService.set(Constant.JWT_REFRESH_TOKEN_BLACKLIST + refreshToken, userId, JWTTokenUtil.getRemainingTime(refreshToken), TimeUnit.MILLISECONDS);
    }

    @Override
    public PageVO<SysUser> pageInfo(UserPageReqVO vo) {
        //传入当前第几页和当前条数
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        List<SysUser> sysUsers = sysUserMapper.selectAll(vo);
        return PageUtils.getPageVO(sysUsers);
    }

    @Override
    public LoginRespVO login(LoginReqVO vo) {
        //通过用户名查询用户信息
        //如果查询存在用户
        //比较密码是否一致
        SysUser userInfoByName = sysUserMapper.getUserInfoByName(vo.getUsername());
        if (null == userInfoByName) {
            throw new BusinessException(BaseResponseCode.ACCOUNT_ERROR);
        }
        if (userInfoByName.getStatus() == 2) {
            throw new BusinessException(BaseResponseCode.ACCOUNT_LOCK);
        }
        if (!PasswordUtils.matches(userInfoByName.getSalt(), vo.getPassword(), userInfoByName.getPassword())) {
            throw new BusinessException(BaseResponseCode.ACCOUNT_PASSWORD_ERROR);
        }
        LoginRespVO loginRespVO = new LoginRespVO();
        loginRespVO.setUsername(userInfoByName.getUsername());
        loginRespVO.setId(userInfoByName.getId());
        loginRespVO.setPhone(userInfoByName.getPhone());
        //存放角色的权限信息和用户id
        Map<String, Object> claims = new HashMap<>();
        claims.put(Constant.JWT_ROLES_KEY, getRolesByUserId(userInfoByName.getId()));
        claims.put(Constant.JWT_PERMISSIONS_KEY, getPermissionsByUserId(userInfoByName.getId()));
        claims.put(Constant.JWT_USER_NAME, userInfoByName.getUsername());
        //签发token
        String accessToken = JWTTokenUtil.getAccessToken(userInfoByName.getId(), claims);
        String refreshToken;
        if (vo.getType().equals("1")) {
            refreshToken = JWTTokenUtil.getRefreshToken(userInfoByName.getId(), claims);
        } else {
            refreshToken = JWTTokenUtil.getRefreshAppToken(userInfoByName.getId(), claims);
        }
        loginRespVO.setAccessToken(accessToken);
        loginRespVO.setRefeshToken(refreshToken);
        return loginRespVO;
    }


    /**
     * mock数据
     * 通过用户id获取该用户所拥有的角色
     *
     * @param userId
     * @return
     */
    private List<String> getRolesByUserId(String userId) {
        List<String> roles = new ArrayList<>();
        if ("9a26f5f1-cbd2-473d-82db-1d6dcf4598f8".equals(userId)) {
            roles.add("admin");
        } else {
            roles.add("test");
        }
        return roles;
    }

    /**
     * 通过mock数据获取用户所拥有的角色
     *
     * @param userId
     * @return
     */
    private List<String> getPermissionsByUserId(String userId) {
        List<String> permissions = new ArrayList<>();
        if ("9a26f5f1-cbd2-473d-82db-1d6dcf4598f8".equals(userId)) {
            permissions.add("sys:user:list");
            permissions.add("sys:user:add");
            permissions.add("sys:user:update");
            permissions.add("sys:user:delete");
        } else {
            permissions.add("sys:user:detail");
        }
        return permissions;
    }

}
