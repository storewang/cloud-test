package com.ai.spring.boot.content.convert;

import com.ai.spring.boot.content.dao.bean.Share;
import com.ai.spring.boot.content.service.dto.ShareDTO;
import com.ai.spring.im.common.convert.EntityConverter;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * 分享Entity与DTO转换
 *
 * @author 石头
 * @Date 2020/3/25
 * @Version 1.0
 **/
@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ShareDTOConvert extends EntityConverter<ShareDTO,Share> {
}
