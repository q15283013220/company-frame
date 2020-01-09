package com.example.companyframe.shiro.filter;

import com.alibaba.druid.sql.visitor.functions.Concat;
import com.alibaba.fastjson.JSON;
import com.example.companyframe.constants.Constant;
import com.example.companyframe.exception.BusinessException;
import com.example.companyframe.exception.code.BaseResponseCode;
import com.example.companyframe.shiro.CustomUsernamePasswordToken;
import com.example.companyframe.utils.DataResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.OutputStream;

@Slf4j
public class CustomAccessControllerFilter extends AccessControlFilter {
    /**
     * 是否允许访问下一层
     * true: 允许，交下一个filte处理
     * false:会往下执行onAccessDenied
     *
     * @param servletRequest
     * @param servletResponse
     * @param o
     * @return
     * @throws Exception
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        return false;
    }

    /**
     * 表示访问拒绝时是自己处理
     * 如果true表示自己不处理且拦截器链继续执行
     * 返回false表示自己已经处理了
     *
     * @param servletRequest
     * @param servletResponse
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        log.info(request.getMethod());
        log.info(request.getRequestURL().toString());
        //判断客户端是否携带accessToken
        try {
            String accessToken = request.getHeader(Constant.ACCESS_TOKEN);
            if (StringUtils.isEmpty(accessToken)) {
                throw new BusinessException(BaseResponseCode.TOKEN_NOT_NULL);
            }
            CustomUsernamePasswordToken customUsernamePasswordToken = new CustomUsernamePasswordToken(accessToken);
            getSubject(servletRequest, servletResponse).login(customUsernamePasswordToken);
        } catch (BusinessException e) {
            customRsponse(e.getCode(), e.getDefaultMessage(), servletResponse);
            return false;
        } catch (AuthenticationException e) {
            if (e.getCause() instanceof BusinessException) {
                BusinessException exception = (BusinessException) e.getCause();
                customRsponse(exception.getCode(), exception.getDefaultMessage(), servletResponse);
            } else {
                customRsponse(BaseResponseCode.SHIRO_AUTHENTICATION_ERROR.getCode()
                        , BaseResponseCode.SHIRO_AUTHENTICATION_ERROR.getMsg(), servletResponse);
            }

            return false;
        }
        return true;
    }

    /**
     * 自定义错误响应
     */
    private void customRsponse(int code, String msg, ServletResponse response) {
        // 自定义异常的类，用户返回给客户端相应的JSON格式的信息
        try {
            DataResult result = DataResult.getResult(code, msg);
            response.setContentType("application/json; charset=utf-8");
            response.setCharacterEncoding("UTF-8");

            String userJson = JSON.toJSONString(result);
            OutputStream out = response.getOutputStream();
            out.write(userJson.getBytes("UTF-8"));
            out.flush();
        } catch (IOException e) {
            log.error("eror={}", e);
        }
    }
}
