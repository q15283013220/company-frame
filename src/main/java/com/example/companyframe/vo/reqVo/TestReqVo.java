package com.example.companyframe.vo.reqVo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class TestReqVo {
    @ApiModelProperty("名称")
    @NotBlank(message = "名称不能为空")
    private String name;

    @ApiModelProperty("测试年龄")
    @NotNull(message = "age 不能为空")
    private Integer age;
    @ApiModelProperty("测试集合")
    @NotEmpty(message = "ids 不能为空")
    private List<String> ids;
}
