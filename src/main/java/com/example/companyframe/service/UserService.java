package com.example.companyframe.service;

import com.example.companyframe.entity.SysUser;
import com.example.companyframe.vo.reqVo.LoginReqVO;
import com.example.companyframe.vo.reqVo.UserPageReqVO;
import com.example.companyframe.vo.respVo.LoginRespVO;
import com.example.companyframe.vo.respVo.PageVO;

import java.util.List;

public interface UserService {
    /**
     * 登录
     *
     * @param vo
     * @return
     */
    LoginRespVO login(LoginReqVO vo);

    /**
     * 登出
     *
     * @param accessToken
     * @param refreshToken
     */
    void logout(String accessToken, String refreshToken);

    PageVO<SysUser> pageInfo(UserPageReqVO vo);
}
