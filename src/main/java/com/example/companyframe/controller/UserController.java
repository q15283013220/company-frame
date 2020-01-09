package com.example.companyframe.controller;

import com.example.companyframe.constants.Constant;
import com.example.companyframe.entity.SysUser;
import com.example.companyframe.exception.code.BaseResponseCode;
import com.example.companyframe.service.UserService;
import com.example.companyframe.utils.DataResult;
import com.example.companyframe.vo.reqVo.LoginReqVO;
import com.example.companyframe.vo.reqVo.UserPageReqVO;
import com.example.companyframe.vo.respVo.LoginRespVO;
import com.example.companyframe.vo.respVo.PageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@Api(tags = "用户模块相关接口")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/user/login")
    @ApiOperation("用户登录接口")
    public DataResult<LoginReqVO> login(@RequestBody @Valid LoginReqVO vo) {
        LoginRespVO login = userService.login(vo);
        return DataResult.success(login);
    }

    @GetMapping("/user/logout")
    @ApiOperation("用户登出接口")
    public DataResult logout(HttpServletRequest request) {
        String accessToken = request.getHeader(Constant.ACCESS_TOKEN);
        String refreshToken = request.getHeader(Constant.REFRESH_TOKEN);
        userService.logout(accessToken, refreshToken);
        return DataResult.success();
    }

    @GetMapping("/user/unLogin")
    @ApiOperation(value = "引导客户端去登录")
    public DataResult unLogin() {
        DataResult result = DataResult.getResult(BaseResponseCode.TOKEN_ERROR);
        return result;
    }

    @PostMapping("/users")
    @ApiOperation("分页查询接口")
    @RequiresPermissions("sys:user:list")
    public DataResult pageInfo(@RequestBody UserPageReqVO vo) {
        PageVO<SysUser> sysUserPageVO = userService.pageInfo(vo);
        return DataResult.success(sysUserPageVO);
    }
}
