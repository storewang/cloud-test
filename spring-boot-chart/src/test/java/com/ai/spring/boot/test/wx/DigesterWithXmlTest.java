package com.ai.spring.boot.test.wx;

import com.ai.spring.boot.im.dto.wxchart.WxEventDTO;
import org.apache.commons.digester3.Digester;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Created by 石头 on 2020/9/12.
 */
public class DigesterWithXmlTest {
    @Test
    public void dealWithWxEvent() throws IOException, SAXException {
        String subscribe = "<xml>" +
                "<ToUserName><![CDATA[gh_f286de58d685]]></ToUserName>" +
                "<FromUserName><![CDATA[oibi0wEMq_NkO4tAobkAPlTtdudY]]></FromUserName>" +
                "<CreateTime>1599923147</CreateTime>" +
                "<MsgType><![CDATA[event]]></MsgType>" +
                "<Event><![CDATA[subscribe]]></Event>" +
                "<EventKey><![CDATA[]]></EventKey>" +
                "</xml>";
        Digester digester = new Digester();
        digester.setValidating(false);

        digester.addObjectCreate("xml", WxEventDTO.class);
        digester.addCallMethod("xml/ToUserName","setToUserName",0);
        digester.addCallMethod("xml/FromUserName","setFromUserName",0);
        digester.addCallMethod("xml/CreateTime","setCreateTime",0,new Class[]{Long.class});
        digester.addCallMethod("xml/MsgType","setMsgType",0);
        digester.addCallMethod("xml/Event","setEvent",0);
        digester.addCallMethod("xml/EventKey","setEventKey",0);

        WxEventDTO result = digester.parse(new ByteArrayInputStream(subscribe.getBytes("UTF-8")));
        System.out.println(result);
        Assert.assertEquals("oibi0wEMq_NkO4tAobkAPlTtdudY",result.getFromUserName());
    }
}
