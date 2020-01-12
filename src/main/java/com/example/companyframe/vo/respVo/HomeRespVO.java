package com.example.companyframe.vo.respVo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class HomeRespVO {
    @ApiModelProperty("用户信息")
    private UserInfoRespVO userInfoVO;

    @ApiModelProperty("首页菜单导航数据")
    private List<PermissionRespNodeVO> menus;
}
