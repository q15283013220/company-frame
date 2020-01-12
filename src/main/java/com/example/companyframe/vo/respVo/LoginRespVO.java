package com.example.companyframe.vo.respVo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LoginRespVO {
    @ApiModelProperty("正常业务token")
    private String accessToken;
    @ApiModelProperty("刷新token")
    private String refreshToken;
    @ApiModelProperty("用户id")
    private String id;
    @ApiModelProperty("手机号")
    private String phone;
    @ApiModelProperty("账号名")
    private String username;
}
