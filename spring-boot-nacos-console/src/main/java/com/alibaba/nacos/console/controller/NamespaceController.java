package com.alibaba.nacos.console.controller;

import com.alibaba.nacos.config.server.service.PersistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * namespace service
 *
 * @author 石头
 * @Date 2020/4/21
 * @Version 1.0
 **/
@RestController
@RequestMapping("/v1/console/namespaces")
public class NamespaceController {
    @Autowired
    private transient PersistService persistService;
}
