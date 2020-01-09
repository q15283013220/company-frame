package com.example.companyframe.utils;

import org.springframework.stereotype.Component;

/**
 * 初始化配置代理类
 */
@Component
public class InitializerUtil {
    public InitializerUtil(TokenSettings tokenSettings) {
        JWTTokenUtil.setTokenSettings(tokenSettings);
    }
}
