package com.example.companyframe.vo.respVo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class PermissionRespNodeVO {
    @ApiModelProperty("主键")
    private String id;
    @ApiModelProperty("跳转地址")
    private String url;
    @ApiModelProperty("菜单权限名称")
    private String title;
    @ApiModelProperty("子集集合")
    private List<?> children;
    @ApiModelProperty("默认展开")
    private boolean spread = true;
}
