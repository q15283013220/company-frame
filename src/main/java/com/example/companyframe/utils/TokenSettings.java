package com.example.companyframe.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * 配置读取类
 */
@Configuration
@ConfigurationProperties(prefix = "jwt")
@Data
public class TokenSettings {
    /**
     * 密钥
     */
    private String secretKey;
    /**
     * access_token过期时间
     */
    private Duration accessTokenExpireTime;
    /**
     * 刷新token过期时间(PC端)
     */
    private Duration refreshTokenExpireTime;
    /**
     * 刷新token过期时间(App端)
     */
    private Duration refreshTokenExpireAppTime;
    /**
     * 签发人
     */
    private String issuer;
}
