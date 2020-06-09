package com.ai.spring.boot.flux.convert;

import com.ai.spring.boot.flux.dao.bean.FluxEchoMessage;
import com.ai.spring.boot.flux.dto.FluxEchoMessageDTO;
import com.ai.spring.im.common.convert.EntityConverter;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * 消息Entity与DTO转换
 *
 * @author 石头
 * @Date 2020/6/9
 * @Version 1.0
 **/
@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FluxEchoMessageDTOConvert extends EntityConverter<FluxEchoMessageDTO,FluxEchoMessage> {
}
