package com.example.companyframe.utils;

import com.example.companyframe.vo.respVo.PageVO;
import com.github.pagehelper.Page;

import java.util.List;

public class PageUtils {

    private PageUtils() {
    }

    public static <T> PageVO<T> getPageVO(List<T> list) {
        PageVO<T> result = new PageVO<>();
        if (list instanceof Page) {
            Page<T> page = (Page<T>) list;
            result.setTotalRows(page.getTotal());
            result.setTotalPages(page.getPages());
            result.setPageNum(page.getPageNum());
            result.setCurPageSize(page.getPageSize());
            result.setPageSize(page.size());
            result.setList(page.getResult());
        }
        return result;
    }

}
