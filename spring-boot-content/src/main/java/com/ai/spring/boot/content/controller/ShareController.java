package com.ai.spring.boot.content.controller;

import com.ai.spring.boot.content.service.ShareService;
import com.ai.spring.boot.content.service.dto.ShareDTO;
import com.ai.spring.im.common.bean.Response;
import com.ai.spring.im.common.util.ResponseBuildUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 分享对外接口
 *
 * @author 石头
 * @Date 2020/3/25
 * @Version 1.0
 **/
@RestController
@RequestMapping("/shares")
@Slf4j
public class ShareController {
    @Autowired
    private ShareService shareService;

    @GetMapping("/{id}")
    public Response<ShareDTO> findById(@PathVariable("id")Long shareId){
        ShareDTO shareDTO = shareService.findById(shareId);
        return ResponseBuildUtil.buildSuccess(shareDTO);
    }
}
