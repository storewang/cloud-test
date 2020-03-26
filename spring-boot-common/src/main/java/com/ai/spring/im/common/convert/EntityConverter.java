package com.ai.spring.im.common.convert;

import java.util.List;

/**
 * Entity与DTO转换
 *
 * @author 石头
 * @Date 2020/3/25
 * @Version 1.0
 **/
public interface EntityConverter<D,E> {
    /**
     * DTO转Entity
     * @param dto
     * @return
     */
    E toEntity(D dto);
    /**
     * Entity转DTO
     * @param entity
     * @return
     */
    D toDto(E entity);
    /**
     * DTO集合转Entity集合
     * @param dtoList
     * @return
     */
    List<E> toEntity(List<D> dtoList);

    /**
     * Entity集合转DTO集合
     * @param entityList
     * @return
     */
    List <D> toDto(List<E> entityList);
}
