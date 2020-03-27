package com.ai.spring.boot.content.controller;

import com.ai.spring.boot.content.service.ShareService;
import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试接口
 *
 * @author 石头
 * @Date 2020/3/26
 * @Version 1.0
 **/
@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {
    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private ShareService shareService;
    @GetMapping("/users")
    public List<ServiceInstance> getInstances(){
        List<ServiceInstance> instances = discoveryClient.getInstances("spring-boot-user");
        return instances;
    }

    @GetMapping("/test-a")
    public String testSentinelA(){
        shareService.testSentinel();
        return "test-sentinel-a";
    }
    @GetMapping("/test-b")
    public String testSentinelB(){
        shareService.testSentinel();
        return "test-sentinel-b";
    }

    @GetMapping("/test-hot")
    @SentinelResource("hot")
    public String testSentinelHot(@RequestParam(required = false) String a,@RequestParam(required = false) String b){
        return a + " " + b;
    }

    @GetMapping("/test-add-flow-rule")
    public String testAddFlowQpsRule(){
        initFlowQpsRule();
        return "success";
    }

    private void initFlowQpsRule(){
        List<FlowRule> rules = new ArrayList<>();
        FlowRule flowRule = new FlowRule("/shares/1");

        flowRule.setCount(10);
        flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        flowRule.setLimitApp("default");
        rules.add(flowRule);
        FlowRuleManager.loadRules(rules);
    }

    /**
     * sentinel代码方式创建监控资源
     * @param a
     * @return
     */
    @GetMapping("/test-sentinel-api")
    public String testSentinelApi(@RequestParam(required = false) String a){
        Entry entry = null;
        String resourceName = "test-sentinel-api";
        ContextUtil.enter(resourceName,"test-wfw");
        try{
            // 定义一个sentinel的保护的资源，名称是test-sentinel-api
            entry = SphU.entry(resourceName);
            if (StringUtils.isEmpty(a)){
                throw new IllegalArgumentException("a参数不能为空");
            }
            return a;
        }catch (BlockException e){
            log.warn("限流，或者降级了",e);
            return "限流，或者降级了";
        }catch (IllegalArgumentException e){
            // sentinel 默认是不统计BlockException及其子类以外的异常， 统计IllegalArgumentException【发生的异常次数，占比等】
            Tracer.trace(e);

            return "参数非法";
        }
        finally {
            if (entry!=null){
                //退出entry
                entry.exit();
            }
            ContextUtil.exit();
        }
    }

    /**
     * sentinel SentinelResource方式创建监控资源
     * @param a
     * @return
     */
    @GetMapping("/test-sentinel-anno")
    @SentinelResource(value = "test-sentinel-api",blockHandler = "block",fallback = "fallback")
    public String testSentinelAnnotation(@RequestParam(required = false) String a){
        if (StringUtils.isEmpty(a)){
            throw new IllegalArgumentException("a can't be blank.");
        }
        return a;
    }
    public String block(String a,BlockException e){
        log.warn("限流，或者降级了 block",e);
        return "限流，或者降级了 block";
    }
    public String fallback(String a,Throwable e){
        log.warn("限流，或者降级了 fallback",e);
        return "限流，或者降级了 fallback";
    }
}
