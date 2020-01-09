package com.example.companyframe.exception.handler;


import com.example.companyframe.exception.BusinessException;
import com.example.companyframe.exception.code.BaseResponseCode;
import com.example.companyframe.utils.DataResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;

/**
 * @ClassName: RestExceptionHandler
 * TODO:类文件简单描述
 * @Author: 小霍
 * @UpdateUser: 小霍
 * @Version: 0.0.1
 */
@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public DataResult exception(Exception e) {
        log.error("Exception,{},{}", e.getLocalizedMessage(), e);
        return DataResult.getResult(BaseResponseCode.SYSTEM_ERROR);
    }

    @ExceptionHandler(value = BusinessException.class)
    public DataResult businessException(BusinessException e) {
        log.error("businessException,{},{}", e.getLocalizedMessage(), e);
        return DataResult.getResult(e.getCode(), e.getDefaultMessage());
    }

    /**
     * 处理validation 框架异常
     *
     * @param e
     * @return com.yingxue.lesson.utils.DataResult<T>
     * @throws
     * @UpdateUser:
     * @Version: 0.0.1
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    <T> DataResult<T> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.error("methodArgumentNotValidExceptionHandler bindingResult.allErrors():{},exception:{}", e.getBindingResult().getAllErrors(), e);
        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        return createValidExceptionResp(errors);
    }

    private <T> DataResult<T> createValidExceptionResp(List<ObjectError> errors) {
        String[] msgs = new String[errors.size()];
        int i = 0;
        for (ObjectError error : errors) {
            msgs[i] = error.getDefaultMessage();
            log.info("msg={}", msgs[i]);
            i++;
        }
        return DataResult.getResult(BaseResponseCode.METHOD_IDENTITY_ERROR.getCode(), msgs[0]);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    <T> DataResult<T> constraintViolationExceptionHandler(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> set = e.getConstraintViolations();
        StringBuffer sb = new StringBuffer();
        for (ConstraintViolation s : set) {
            sb.append(s.getMessageTemplate());
            sb.append(",");
        }
        //去掉最后一个","
        String msg = sb.toString().substring(0, sb.length() - 1);
        return DataResult.getResult(BaseResponseCode.METHOD_IDENTITY_ERROR.getCode(), msg);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public DataResult unauthorizedException(UnauthorizedException e) {
        log.error("UnauthorizedException,{},{}", e.getLocalizedMessage(), e);
        return DataResult.getResult(BaseResponseCode.NOT_PERMISSION);
    }
}
