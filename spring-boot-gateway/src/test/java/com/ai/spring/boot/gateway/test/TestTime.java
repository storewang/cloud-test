package com.ai.spring.boot.gateway.test;

import org.junit.Test;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * TODO
 *
 * @author 石头
 * @Date 2020/3/30
 * @Version 1.0
 **/
public class TestTime {
    @Test
    public void testGateWayTime(){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
        System.out.println(dateTimeFormatter.format(LocalTime.now()));
    }
}
