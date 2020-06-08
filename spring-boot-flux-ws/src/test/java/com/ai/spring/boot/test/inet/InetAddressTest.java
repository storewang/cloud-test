package com.ai.spring.boot.test.inet;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * @author 石头
 * @Date 2020/6/8
 * @Version 1.0
 **/
@Slf4j
public class InetAddressTest {
    @Test
    public void testGetLocalHostAddress(){
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            String hostAddress = localHost.getHostAddress();
            String hostName = localHost.getHostName();
            System.out.println("-------hostAddress:-------------"+hostAddress);
            System.out.println("-------hostName:-------------"+hostName);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }
}
