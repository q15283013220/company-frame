package com.example.companyframe.controller;

import com.example.companyframe.exception.code.BaseResponseCode;
import com.example.companyframe.utils.DataResult;
import com.example.companyframe.vo.reqVo.TestReqVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;


@RestController
@RequestMapping("/test")
@Api(tags = "测试相关接口")
@Validated
public class TestController {

    @GetMapping("index")
    @ApiOperation("swagger 测试")
    public String index() {
        return "Hello";
    }

    @GetMapping("home")
    @ApiOperation(value = "测试同一返回格式接口")
    public DataResult<String> getHome() {
//        DataResult<String> result = DataResult.success();
        DataResult<String> result = DataResult.getResult(BaseResponseCode.SUCCESS);
        result.setData("请求成功");
//        throw new BusinessException(BaseResponseCode.DATA_ERROR);
        return result;
    }

    @PostMapping("valid/error")
    @ApiOperation(value = "测试Validator抛出业务异常")
    public DataResult testValid(@RequestBody @Valid TestReqVo vo) {
        DataResult result = DataResult.success();
        return result;
    }

    @PostMapping("valid/error2")
    @ApiOperation(value = "测试Validator抛出业务异常")
    public DataResult testValid2(@ApiParam("测试参数为空抛出的异常") @NotBlank(message = "name不能为空") String name, @NotBlank(message = "密码不能为空") String password) {
        System.out.println(name);
        DataResult result = DataResult.success();
        return result;
    }
}
