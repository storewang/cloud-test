package com.ai.spring.boot.content.dao.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分享信息
 *
 * @author 石头
 * @Date 2020/3/25
 * @Version 1.0
 **/
@Data
@NoArgsConstructor
public class Share {
    private Long shareId;
    private String title;
    private Boolean isOriginal;
    private String author;
    private String conver;
    private String summary;
    private Integer price;
    private String auditStatus;

    private Long userId;

    public static Share builder(){
        return new Share();
    }

    public Share shareId(Long shareId){
        setShareId(shareId);
        return this;
    }
    public Share userId(Long userId){
        setUserId(userId);
        return this;
    }
    public Share price(Integer price){
        setPrice(price);
        return this;
    }
    public Share title(String title){
        setTitle(title);
        return this;
    }
    public Share isOriginal(Boolean isOriginal){
        setIsOriginal(isOriginal);
        return this;
    }
    public Share author(String author){
        setAuthor(author);
        return this;
    }
    public Share conver(String conver){
        setConver(conver);
        return this;
    }
    public Share summary(String summary){
        setSummary(summary);
        return this;
    }
    public Share auditStatus(String auditStatus){
        setAuditStatus(auditStatus);
        return this;
    }
}
