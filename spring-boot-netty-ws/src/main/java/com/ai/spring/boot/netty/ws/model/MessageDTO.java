package com.ai.spring.boot.netty.ws.model;

import com.ai.spring.boot.netty.ws.util.Consts;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 消息传输体
 *
 * @author 石头
 * @Date 2020/6/29
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@Builder
public class MessageDTO {
     private UserDTO from;
     private UserDTO to;
     private List<UserDTO> tos;
     /**消息内容*/
     private String content;
     private Integer msgType;
     private Date createTime;
     private String msgId;

     public MessageDTO(){
         this.msgId = Consts.getMsgReqId();
         this.createTime = Calendar.getInstance().getTime();
     }

     public MessageDTO(UserDTO from,String msg,Integer msgType){
         this.from   = from;
         this.content= msg;
         this.msgType= msgType;
         this.msgId  = Consts.getMsgReqId();
         this.createTime = Calendar.getInstance().getTime();
     }

    public MessageDTO(UserDTO from,UserDTO to, String msg,Integer msgType){
        this.from   = from;
        this.to     = to;
        this.content= msg;
        this.msgType= msgType;
        this.msgId  = Consts.getMsgReqId();
        this.createTime = Calendar.getInstance().getTime();
    }
}
