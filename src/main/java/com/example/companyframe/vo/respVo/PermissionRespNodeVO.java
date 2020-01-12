package com.example.companyframe.vo.respVo;

import lombok.Data;

import java.util.List;

@Data
public class PermissionRespNodeVO {

    private String id;

    private String url;

    private String title;

    private List<?> children;
}
