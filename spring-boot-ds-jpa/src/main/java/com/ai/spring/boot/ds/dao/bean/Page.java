package com.ai.spring.boot.ds.dao.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 翻页对象
 *
 * @author 石头
 * @Date 2020/6/9
 * @Version 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Page<T> {
    private Integer pageSize;
    private Integer currentPage;
    private Integer totalPage;
    private Long totalSize;
    private List<T> data;
    public Page(List<T> data){
        this.data = data;
    }
    public static <E>Page<E> of(List<E> data){
        return new Page(data);
    }
}
