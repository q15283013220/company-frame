package com.example.companyframe.controller;

import com.example.companyframe.constants.Constant;
import com.example.companyframe.service.HomeService;
import com.example.companyframe.utils.DataResult;
import com.example.companyframe.utils.JWTTokenUtil;
import com.example.companyframe.vo.respVo.HomeRespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
@Api(tags = "首页模块")
public class HomeController {

    @Autowired
    private HomeService homeService;

    @GetMapping("/home")
    @ApiOperation("获取首页数据")
    public DataResult<HomeRespVO> getHome(HttpServletRequest request) {
        String accessToken = request.getHeader(Constant.ACCESS_TOKEN);
        String userId = JWTTokenUtil.getUserId(accessToken);
        HomeRespVO home = homeService.getHome(userId);
        return DataResult.success(home);
    }
}
