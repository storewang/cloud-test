package com.alibaba.csp.sentinel.dashboard.controller.gateway;

import com.alibaba.csp.sentinel.dashboard.dao.dto.MachineInfo;
import com.alibaba.csp.sentinel.dashboard.dao.entity.gateway.ApiDefinitionEntity;
import com.alibaba.csp.sentinel.dashboard.dao.entity.gateway.ApiPredicateItemEntity;
import com.alibaba.csp.sentinel.dashboard.dao.mapper.gateway.InMemApiDefinitionStore;
import com.alibaba.csp.sentinel.dashboard.service.auth.AuthService;
import com.alibaba.csp.sentinel.dashboard.service.client.SentinelApiClient;
import com.alibaba.csp.sentinel.dashboard.vo.Result;
import com.alibaba.csp.sentinel.dashboard.vo.gateway.AddApiReqVo;
import com.alibaba.csp.sentinel.dashboard.vo.gateway.ApiPredicateItemVo;
import com.alibaba.csp.sentinel.dashboard.vo.gateway.UpdateApiReqVo;
import com.alibaba.csp.sentinel.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

import static com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants.URL_MATCH_STRATEGY_EXACT;
import static com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX;
import static com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants.URL_MATCH_STRATEGY_REGEX;


/**
 * Gateway api Controller for manage gateway api definitions.
 *
 * @author cdfive
 * @since 1.7.0
 */
@RestController
@RequestMapping(value = "/gateway/api")
public class GatewayApiController {

    private final Logger logger = LoggerFactory.getLogger(GatewayApiController.class);

    @Autowired
    private InMemApiDefinitionStore repository;

    @Autowired
    private SentinelApiClient sentinelApiClient;

    @Autowired
    private AuthService<HttpServletRequest> authService;

    @GetMapping("/list.json")
    public Result<List<ApiDefinitionEntity>> queryApis(HttpServletRequest request, String app, String ip, Integer port) {
        AuthService.AuthUser authUser = authService.getAuthUser(request);
        authUser.authTarget(app, AuthService.PrivilegeType.READ_RULE);

        if (StringUtil.isEmpty(app)) {
            return Result.ofFail(-1, "app can't be null or empty");
        }
        if (StringUtil.isEmpty(ip)) {
            return Result.ofFail(-1, "ip can't be null or empty");
        }
        if (port == null) {
            return Result.ofFail(-1, "port can't be null");
        }

        try {
            List<ApiDefinitionEntity> apis = sentinelApiClient.fetchApis(app, ip, port).get();
            repository.saveAll(apis);
            return Result.ofSuccess(apis);
        } catch (Throwable throwable) {
            logger.error("queryApis error:", throwable);
            return Result.ofThrowable(-1, throwable);
        }
    }

    @PostMapping("/new.json")
    public Result<ApiDefinitionEntity> addApi(HttpServletRequest request, @RequestBody AddApiReqVo reqVo) {
        AuthService.AuthUser authUser = authService.getAuthUser(request);

        String app = reqVo.getApp();
        if (StringUtil.isBlank(app)) {
            return Result.ofFail(-1, "app can't be null or empty");
        }

        authUser.authTarget(app, AuthService.PrivilegeType.WRITE_RULE);

        ApiDefinitionEntity entity = new ApiDefinitionEntity();
        entity.setApp(app.trim());

        String ip = reqVo.getIp();
        if (StringUtil.isBlank(ip)) {
            return Result.ofFail(-1, "ip can't be null or empty");
        }
        entity.setIp(ip.trim());

        Integer port = reqVo.getPort();
        if (port == null) {
            return Result.ofFail(-1, "port can't be null");
        }
        entity.setPort(port);

        // API名称
        String apiName = reqVo.getApiName();
        if (StringUtil.isBlank(apiName)) {
            return Result.ofFail(-1, "apiName can't be null or empty");
        }
        entity.setApiName(apiName.trim());

        // 匹配规则列表
        List<ApiPredicateItemVo> predicateItems = reqVo.getPredicateItems();
        if (CollectionUtils.isEmpty(predicateItems)) {
            return Result.ofFail(-1, "predicateItems can't empty");
        }

        List<ApiPredicateItemEntity> predicateItemEntities = new ArrayList<>();
        for (ApiPredicateItemVo predicateItem : predicateItems) {
            ApiPredicateItemEntity predicateItemEntity = new ApiPredicateItemEntity();

            // 匹配模式
            Integer matchStrategy = predicateItem.getMatchStrategy();
            if (!Arrays.asList(URL_MATCH_STRATEGY_EXACT, URL_MATCH_STRATEGY_PREFIX, URL_MATCH_STRATEGY_REGEX).contains(matchStrategy)) {
                return Result.ofFail(-1, "invalid matchStrategy: " + matchStrategy);
            }
            predicateItemEntity.setMatchStrategy(matchStrategy);

            // 匹配串
            String pattern = predicateItem.getPattern();
            if (StringUtil.isBlank(pattern)) {
                return Result.ofFail(-1, "pattern can't be null or empty");
            }
            predicateItemEntity.setPattern(pattern);

            predicateItemEntities.add(predicateItemEntity);
        }
        entity.setPredicateItems(new LinkedHashSet<>(predicateItemEntities));

        // 检查API名称不能重复
        List<ApiDefinitionEntity> allApis = repository.findAllByMachine(MachineInfo.of(app.trim(), ip.trim(), port));
        if (allApis.stream().map(o -> o.getApiName()).anyMatch(o -> o.equals(apiName.trim()))) {
            return Result.ofFail(-1, "apiName exists: " + apiName);
        }

        Date date = new Date();
        entity.setGmtCreate(date);
        entity.setGmtModified(date);

        try {
            entity = repository.save(entity);
        } catch (Throwable throwable) {
            logger.error("add gateway api error:", throwable);
            return Result.ofThrowable(-1, throwable);
        }

        if (!publishApis(app, ip, port)) {
            logger.warn("publish gateway apis fail after add");
        }

        return Result.ofSuccess(entity);
    }

    @PostMapping("/save.json")
    public Result<ApiDefinitionEntity> updateApi(HttpServletRequest request, @RequestBody UpdateApiReqVo reqVo) {
        AuthService.AuthUser authUser = authService.getAuthUser(request);

        String app = reqVo.getApp();
        if (StringUtil.isBlank(app)) {
            return Result.ofFail(-1, "app can't be null or empty");
        }

        authUser.authTarget(app, AuthService.PrivilegeType.WRITE_RULE);

        Long id = reqVo.getId();
        if (id == null) {
            return Result.ofFail(-1, "id can't be null");
        }

        ApiDefinitionEntity entity = repository.findById(id);
        if (entity == null) {
            return Result.ofFail(-1, "api does not exist, id=" + id);
        }

        // 匹配规则列表
        List<ApiPredicateItemVo> predicateItems = reqVo.getPredicateItems();
        if (CollectionUtils.isEmpty(predicateItems)) {
            return Result.ofFail(-1, "predicateItems can't empty");
        }

        List<ApiPredicateItemEntity> predicateItemEntities = new ArrayList<>();
        for (ApiPredicateItemVo predicateItem : predicateItems) {
            ApiPredicateItemEntity predicateItemEntity = new ApiPredicateItemEntity();

            // 匹配模式
            int matchStrategy = predicateItem.getMatchStrategy();
            if (!Arrays.asList(URL_MATCH_STRATEGY_EXACT, URL_MATCH_STRATEGY_PREFIX, URL_MATCH_STRATEGY_REGEX).contains(matchStrategy)) {
                return Result.ofFail(-1, "Invalid matchStrategy: " + matchStrategy);
            }
            predicateItemEntity.setMatchStrategy(matchStrategy);

            // 匹配串
            String pattern = predicateItem.getPattern();
            if (StringUtil.isBlank(pattern)) {
                return Result.ofFail(-1, "pattern can't be null or empty");
            }
            predicateItemEntity.setPattern(pattern);

            predicateItemEntities.add(predicateItemEntity);
        }
        entity.setPredicateItems(new LinkedHashSet<>(predicateItemEntities));

        Date date = new Date();
        entity.setGmtModified(date);

        try {
            entity = repository.save(entity);
        } catch (Throwable throwable) {
            logger.error("update gateway api error:", throwable);
            return Result.ofThrowable(-1, throwable);
        }

        if (!publishApis(app, entity.getIp(), entity.getPort())) {
            logger.warn("publish gateway apis fail after update");
        }

        return Result.ofSuccess(entity);
    }

    @PostMapping("/delete.json")
    public Result<Long> deleteApi(HttpServletRequest request, Long id) {
        AuthService.AuthUser authUser = authService.getAuthUser(request);

        if (id == null) {
            return Result.ofFail(-1, "id can't be null");
        }

        ApiDefinitionEntity oldEntity = repository.findById(id);
        if (oldEntity == null) {
            return Result.ofSuccess(null);
        }

        authUser.authTarget(oldEntity.getApp(), AuthService.PrivilegeType.DELETE_RULE);

        try {
            repository.delete(id);
        } catch (Throwable throwable) {
            logger.error("delete gateway api error:", throwable);
            return Result.ofThrowable(-1, throwable);
        }

        if (!publishApis(oldEntity.getApp(), oldEntity.getIp(), oldEntity.getPort())) {
            logger.warn("publish gateway apis fail after delete");
        }

        return Result.ofSuccess(id);
    }

    private boolean publishApis(String app, String ip, Integer port) {
        List<ApiDefinitionEntity> apis = repository.findAllByMachine(MachineInfo.of(app, ip, port));
        return sentinelApiClient.modifyApis(app, ip, port, apis);
    }
}
