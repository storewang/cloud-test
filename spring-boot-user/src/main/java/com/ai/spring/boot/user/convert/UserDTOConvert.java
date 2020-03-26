package com.ai.spring.boot.user.convert;

import com.ai.spring.boot.user.dao.bean.User;
import com.ai.spring.boot.user.service.dto.UserDTO;
import com.ai.spring.im.common.convert.EntityConverter;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * 用户Entity与DTO转换
 *
 * @author 石头
 * @Date 2020/3/25
 * @Version 1.0
 **/
@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserDTOConvert extends EntityConverter<UserDTO,User> {
}
