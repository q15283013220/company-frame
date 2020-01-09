package com.example.companyframe.vo.reqVo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginReqVO {
    @ApiModelProperty("账号")
    @NotBlank(message = "账号不能为空")
    private String username;
    @ApiModelProperty("密码")
    @NotBlank(message = "密码不能为空")
    private String password;
    @NotBlank(message = "用户登录类型不能为空")
    @ApiModelProperty("登录类型 1:PC 2:App")
    private String type;
}
