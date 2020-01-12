package com.example.companyframe.vo.respVo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserInfoRespVO {
    @ApiModelProperty("id")
    private String id;
    @ApiModelProperty("账号")
    private String username;
    @ApiModelProperty("部门id")
    private String deptId;
    @ApiModelProperty("所属部门名称")
    private String deptName;
}
