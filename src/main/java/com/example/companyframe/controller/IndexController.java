package com.example.companyframe.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("index")
@Api(tags = "视图")
public class IndexController {

    @GetMapping("/404")
    @ApiOperation("跳转404错误页面")
    public String error404() {
        return "error/404";
    }

    @GetMapping("login")
    @ApiOperation("跳转登录页面")
    public String login() {
        return "login";
    }

    @GetMapping("/home")
    @ApiOperation("跳转首页页面")
    public String home() {
        return "home";
    }

    @GetMapping("/main")
    @ApiOperation("跳转主页")
    public String main() {
        return "main";
    }

    @GetMapping("/menus")
    @ApiOperation(value = "跳转菜单权限管理页面")
    public String menus() {
        return "menus/menu";
    }
}
