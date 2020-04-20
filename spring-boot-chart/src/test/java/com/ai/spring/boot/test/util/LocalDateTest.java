package com.ai.spring.boot.test.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * TODO
 *
 * @author 石头
 * @Date 2020/4/20
 * @Version 1.0
 **/
@Slf4j
public class LocalDateTest {
    @Test
    public void testLocalDateTime(){
        LocalDateTime localDateTime = LocalDateTime.now();
        log.info("------当前日期时间:{}---------",localDateTime);
        localDateTime = LocalDateTime.of(2019, Month.SEPTEMBER, 10, 14, 46, 56);
        log.info("------当前日期时间:{}---------",localDateTime);
        LocalDate localDate = LocalDate.of(2019,1,7);
        LocalTime localTime = LocalTime.of(12,35,59);
        localDateTime = LocalDateTime.of(localDate, localTime);
        log.info("------当前日期时间:{}---------",localDateTime);

        System.out.println("---------------------------------------------------");
        log.info("------获取当前日期是所在月的第几天:{}---------",localDateTime.getDayOfMonth());
        log.info("------获取当前日期是星期几（星期的英文全称）:{}---------",localDateTime.getDayOfWeek());
        log.info("------获取当前日期是所在年的第几天:{}---------",localDateTime.getDayOfYear());
        log.info("------获取当前日期所在月份（月份的英文全称）:{}---------",localDateTime.getMonth());
        log.info("------获取当前日期所在月份的数值:{}---------",localDateTime.getMonthValue());
        log.info("------获取当前时间的小时数:{}---------",localDateTime.getHour());
        log.info("------获取当前时间的分钟数:{}---------",localDateTime.getMinute());
        log.info("------获取当前时间的秒数:{}---------",localDateTime.getSecond());
        System.out.println("---------------------------------------------------");
        log.info("------将参数中的\"日\"替换localDate中的\"日\":{}---------",localDateTime.withDayOfMonth(2));
        log.info("------将参数中的天数替换localDate中的天数:{}---------",localDateTime.withDayOfYear(40));
        log.info("------将参数中的\"月\"替换localDate中的\"月\":{}---------",localDateTime.withMonth(2));
        log.info("------将参数中的\"年\"替换localDate中的\"年\":{}---------",localDateTime.withYear(2020));
        log.info("------将参数中的\"小时\"替换localTime中的\"小时\":{}---------",localDateTime.withHour(1));
        log.info("------将参数中的\"分钟\"替换localTime中的\"分钟\":{}---------",localDateTime.withMinute(1));
        log.info("------将参数中的\"秒\"替换localTime中的\"秒\":{}---------",localDateTime.withSecond(1));
        System.out.println("---------------------------------------------------");
        log.info("------将当前日期减一天:{}---------",localDateTime.minusDays(1));
        log.info("------将当前日期减一周:{}---------",localDateTime.minusMonths(1));
        log.info("------将当前日期减一月:{}---------",localDateTime.minusWeeks(1));
        log.info("------将当前日期减一年:{}---------",localDateTime.minusYears(1));
        log.info("------将当前日期加一天:{}---------",localDateTime.plusDays(1));
        log.info("------将当前日期加一月:{}---------",localDateTime.plusMonths(1));
        log.info("------将当前日期加一周:{}---------",localDateTime.plusWeeks(1));
        log.info("------将当前日期加一年:{}---------",localDateTime.plusYears(1));
        log.info("------将当前时间减一小时:{}---------",localDateTime.minusHours(1));
        log.info("------将当前时间减一分钟:{}---------",localDateTime.minusMinutes(1));
        log.info("------将当前时间减一秒:{}---------",localDateTime.minusSeconds(1));
        log.info("------将当前时间加一小时:{}---------",localDateTime.plusHours(1));
        log.info("------将当前时间加一分钟:{}---------",localDateTime.plusMinutes(1));
        log.info("------将当前时间加一秒:{}---------",localDateTime.plusSeconds(1));

        System.out.println("---------------------------------------------------");
        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy");
        log.info("------格式转换成 yyyy:{}---------",localDateTime.format(f));
        f = DateTimeFormatter.ofPattern("yyyy-MM");
        log.info("------格式转换成 yyyy-MM:{}---------",localDateTime.format(f));
        f = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        log.info("------格式转换成 yyyy-MM-dd:{}---------",localDateTime.format(f));
        f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        log.info("------格式转换成 yyyy-MM-dd HH:mm:ss:{}---------",localDateTime.format(f));
        f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        log.info("------格式转换成 yyyy-MM-dd HH:mm:{}---------",localDateTime.format(f));
        f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");
        log.info("------格式转换成 yyyy-MM-dd HH:{}---------",localDateTime.format(f));


        System.out.println("---------------------------------------------------");
        log.info("------获取LocalDate:{}---------",localDateTime.toLocalDate());
        log.info("------获取LocalTime:{}---------",localDateTime.toLocalTime());
        Instant instant = Instant.now();
        log.info("------获取秒数:{}---------",instant.getEpochSecond());
        log.info("------获取毫秒数:{}---------",instant.toEpochMilli());

        System.out.println("---------------------------------------------------");
        log.info("------将日期字符串转换成localDateTime类型 :{}---------",LocalDateTime.parse("2019-10-15 22:35:40",DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        System.out.println("---------------------------------------------------");
        // java.util.Date --> java.time.LocalDateTime
        java.util.Date date = new java.util.Date();
        instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        localDateTime = LocalDateTime.ofInstant(instant, zone);
        log.info("------java.util.Date转换成localDateTime:{}---------",localDateTime);

        // java.time.LocalDateTime --> java.util.Date
        localDateTime = LocalDateTime.now();
        zone = ZoneId.systemDefault();
        instant = localDateTime.atZone(zone).toInstant();
        date = Date.from(instant);
        log.info("------localDateTime转换成java.util.Date:{}---------",date);
    }
    @Test
    public void testLocalDateTimeJoin(){
        LocalDate localDate = LocalDate.of(2019,1,7);
        log.info("------当前日期时间:{}---------",localDate.atTime(LocalTime.now()));

        LocalTime localTime = LocalTime.of(12,35,59);
        log.info("------当前日期时间:{}---------",localTime.atDate(LocalDate.of(2019,1,7)));
        // 原来atTime和atDate方法的返回值都是LocalDateTime类，这个类把toString()方法重写了
        // @Override
        // public String toString() {
        //     return date.toString() + 'T' + time.toString();
        // }

        // 格式转换
        // 将LocalDate类型的数据转换为String类型的数据，参数为DateTimeFormatter类，该类提供了ofPattern(String pattern)方法，可传入相应的日期格式：
        // yyyy，
        // yyyy-MM，
        // yyyy-MM-dd，
        // yyyy-MM-dd HH，
        // yyyy-MM-dd HH:mm，
        // yyyy-MM-dd HH:mm:ss
        // 注：若LocalDate未调用atTime方法，则表示LocalDate只表示日期，则此时不能转换带有时间格式的表示式

        localDate = LocalDate.of(2019,1,7);
        localTime = LocalTime.now();
        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy");
        System.out.println("---------------------------------------------------");
        log.info("------格式转换成 yyyy:{}---------",localDate.atTime(localTime).format(f));
        f = DateTimeFormatter.ofPattern("yyyy-MM");
        log.info("------格式转换成 yyyy-MM:{}---------",localDate.atTime(localTime).format(f));
        f = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        log.info("------格式转换成 yyyy-MM-dd:{}---------",localDate.atTime(localTime).format(f));
        f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        log.info("------格式转换成 yyyy-MM-dd HH:mm:ss:{}---------",localDate.atTime(localTime).format(f));
        f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        log.info("------格式转换成 yyyy-MM-dd HH:mm:{}---------",localDate.atTime(localTime).format(f));
        f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");
        log.info("------格式转换成 yyyy-MM-dd HH:{}---------",localDate.atTime(localTime).format(f));

        System.out.println("---------------------------------------------------");
        // 将LocalTime类型的数据转换为String类型的数据，参数为DateTimeFormatter类，该类提供了ofPattern(String pattern)方法，
        // 可传入相应的日期格式：
        // yyyy，
        // yyyy-MM，
        // yyyy-MM-dd，
        // yyyy-MM-dd HH，
        // yyyy-MM-dd HH:mm，
        // yyyy-MM-dd HH:mm:ss
        // 注：若LocalTime未调用atDate方法，则表示LocalTime只表示时间，则此时不能转换带有日期格式的表示式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        log.info("------格式转换成 yyyy-MM-dd HH:mm:ss :{}---------",LocalTime.now().atDate(LocalDate.now()).format(formatter));

        System.out.println("---------------------------------------------------");
        // 将日期字符串转换成LocalDate类型，不能转换带时间格式的字符串
        log.info("------将日期字符串转换成LocalDate类型，不能转换带时间格式的字符串 :{}---------",LocalDate.parse("2019-10-15"));
        log.info("------将日期字符串转换成LocalDate类型，不能转换带时间格式的字符串 :{}---------",LocalDate.parse("2019-10-15 22:35:40",DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        log.info("------将时间字符串转换成LocalTime类型，不能转换带日期格式的字符串 :{}---------",LocalTime.parse("22:35:40"));
        log.info("------将日期字符串转换成LocalTime类型，不能转换带时间格式的字符串 :{}---------",LocalTime.parse("2019-10-15 22:35:40",DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
    @Test
    public void testLocalDate(){
        // 获取当前日期
        LocalDate localDate = LocalDate.now();
        log.info("------当前日期:{}---------",localDate);
        localDate = LocalDate.of(2019,1,7);
        log.info("------根据参数设置日期:{}---------",localDate);

        System.out.println("---------------------------------------------------");
        localDate = LocalDate.of(2019,1,7);
        log.info("------获取当前日期是所在月的第几天:{}---------",localDate.getDayOfMonth());
        log.info("------获取当前日期是星期几（星期的英文全称）:{}---------",localDate.getDayOfWeek());
        log.info("------获取当前日期是所在年的第几天:{}---------",localDate.getDayOfYear());

        System.out.println("---------------------------------------------------");
        localDate = LocalDate.of(2019,1,7);
        log.info("------获取当前日期所在月份（月份的英文全称）:{}---------",localDate.getMonth());
        log.info("------获取当前日期所在月份的数值:{}---------",localDate.getMonthValue());
        log.info("------获取当前日期所在月份有多少天:{}---------",localDate.lengthOfMonth());

        System.out.println("---------------------------------------------------");
        localDate = LocalDate.of(2019,1,7);
        log.info("------获取当前日期所在年有多少天:{}---------",localDate.lengthOfYear());
        log.info("------获取当前日期所在年是否是闰年:{}---------",localDate.isLeapYear());


        // with开头的方法，我的理解是将参数替换localDate中的对应属性，重新计算得到新的日期。
        localDate = LocalDate.of(2019,1,7);
        System.out.println("---------------------------------------------------");
        log.info("------将参数中的\"日\"替换localDate中的\"日\":{}---------",localDate.withDayOfMonth(2));
        log.info("------将参数中的天数替换localDate中的天数:{}---------",localDate.withDayOfYear(40));
        log.info("------将参数中的\"月\"替换localDate中的\"月\":{}---------",localDate.withMonth(2));
        log.info("------将参数中的\"年\"替换localDate中的\"年\":{}---------",localDate.withYear(2020));

        localDate = LocalDate.of(2019,1,7);
        System.out.println("---------------------------------------------------");
        log.info("------将当前日期减一天:{}---------",localDate.minusDays(1));
        log.info("------将当前日期减一周:{}---------",localDate.minusMonths(1));
        log.info("------将当前日期减一月:{}---------",localDate.minusWeeks(1));
        log.info("------将当前日期减一年:{}---------",localDate.minusYears(1));
        log.info("------将当前日期加一天:{}---------",localDate.plusDays(1));
        log.info("------将当前日期加一月:{}---------",localDate.plusMonths(1));
        log.info("------将当前日期加一周:{}---------",localDate.plusWeeks(1));
        log.info("------将当前日期加一年:{}---------",localDate.plusYears(1));
    }

    @Test
    public void testLocalTime(){
        // 获取当前时间
        LocalTime localTime = LocalTime.now();
        log.info("------获取当前时间:{}---------",localTime);
        localTime = LocalTime.of(12,35);
        log.info("------根据参数设置时间，参数分别为时，分:{}---------",localTime);
        localTime = LocalTime.of(12,35,59);
        log.info("------根据参数设置时间，参数分别为时，分，秒:{}---------",localTime);

        System.out.println("----------------12:35:59-----------------------------------");
        log.info("------获取当前时间的小时数:{}---------",localTime.getHour());
        log.info("------获取当前时间的分钟数:{}---------",localTime.getMinute());
        log.info("------获取当前时间的秒数:{}---------",localTime.getSecond());

        // with开头的方法，我的理解是将参数替换localTime中的对应属性，重新计算得到新的时间。
        System.out.println("----------------12:35:59-----------------------------------");
        log.info("------将参数中的\"小时\"替换localTime中的\"小时\":{}---------",localTime.withHour(1));
        log.info("------将参数中的\"分钟\"替换localTime中的\"分钟\":{}---------",localTime.withMinute(1));
        log.info("------将参数中的\"秒\"替换localTime中的\"秒\":{}---------",localTime.withSecond(1));


        System.out.println("----------------12:35:59-----------------------------------");
        log.info("------将当前时间减一小时:{}---------",localTime.minusHours(1));
        log.info("------将当前时间减一分钟:{}---------",localTime.minusMinutes(1));
        log.info("------将当前时间减一秒:{}---------",localTime.minusSeconds(1));
        log.info("------将当前时间加一小时:{}---------",localTime.plusHours(1));
        log.info("------将当前时间加一分钟:{}---------",localTime.plusMinutes(1));
        log.info("------将当前时间加一秒:{}---------",localTime.plusSeconds(1));
    }

}
