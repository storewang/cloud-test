package com.ai.spring.boot.ds.dao.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体基类
 *
 * @author 石头
 * @Date 2020/6/4
 * @Version 1.0
 **/
public abstract class BaseEntity implements Serializable {

    public Long getId() {
        return null;
    }

    public void setId(Long id) {
    }

    public Integer getStatus() {
        return null;
    }

    public void setStatus(Integer status) {
    }

    public Date getCreateTime() {
        return null;
    }

    public void setCreateTime(Date createTime) {
    }

    public Date getUpdateTime() {
        return null;
    }

    public void setUpdateTime(Date updateTime) {
    }
}
