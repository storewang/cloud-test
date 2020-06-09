package com.ai.spring.boot.ds.dao;

import com.ai.spring.boot.ds.dao.bean.BaseEntity;
import com.ai.spring.boot.ds.dao.bean.Page;
import com.ai.spring.boot.ds.dao.repository.BaseJpaRepository;
import com.ai.spring.boot.ds.jpa.IQueryCriteria;
import com.ai.spring.boot.ds.jpa.SpecificationBuilder;
import com.ai.spring.im.common.enums.DataStatusEnum;
import com.ai.spring.im.common.enums.ExceptionEnum;
import com.ai.spring.im.common.exception.DataException;
import com.ai.spring.im.common.util.CopyUtil;
import com.ai.spring.im.common.util.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Jpa操作类
 *
 * @author 石头
 * @Date 2020/6/4
 * @Version 1.0
 **/
@Slf4j
public class BaseJpaDao<M extends BaseJpaRepository<T>,T extends BaseEntity> implements IJpaDao<T>{
    private static final Integer BATCH_SAVE_SIZE = 100;
    @Autowired
    protected M baseRespository;

    protected M getRespository(){
        return baseRespository;
    }

    public T save(T entity){
        // 设置主键
        entity.setId(IdWorker.getId());
        return baseRespository.save(entity);
    }

    /**
     * 批量添加，一次100条数据提交
     * @param entitys
     * @return
     */
    public List<T> batchSave(List<T> entitys){
        if (CollectionUtils.isEmpty(entitys)){
            return Collections.EMPTY_LIST;
        }
        if (entitys.size() <= BATCH_SAVE_SIZE){
            // 设置主键
            entitys.stream().forEach(entity -> entity.setId(IdWorker.getId()));
            return baseRespository.saveAll(entitys);
        }

        // 添加的结果
        List<T> result       = new ArrayList<>();
        // 批量数据标识
        AtomicInteger updNum = new AtomicInteger(0);
        // 需要添加的数据
        List<T> updDatas     = new ArrayList<>();
        for (T entity:entitys){
            // 设置主键
            entity.setId(IdWorker.getId());
            if (updNum.getAndIncrement() <= 100){
                updDatas.add(entity);
            }else {
                result.addAll(baseRespository.saveAll(updDatas));
                updDatas.clear();

                updDatas.add(entity);
                updNum.set(1);
            }
        }
        if (updDatas.size()>0){
            result.addAll(getRespository().saveAll(updDatas));
        }
        return result;
    }
    public T updateBySenstive(T entity){
        Optional<T> optional = baseRespository.findById(entity.getId());
        if (!optional.isPresent()){
            throw new DataException(ExceptionEnum.DATA_NOT_EXIST);
        }
        T data = optional.get();

        CopyUtil.copyWithNoNullFieldVal(entity,data);

        return baseRespository.save(data);
    }

    public T findById(Long id){
        Optional<T> optional = baseRespository.findById(id);
        if (!optional.isPresent()){
            throw new DataException(ExceptionEnum.DATA_NOT_EXIST);
        }

        return optional.get();
    }
    public List<T> findByIds(List<Long> ids){
        return baseRespository.findAllById(ids);
    }
    public boolean removeById(Long id){
        Optional<T> optional = baseRespository.findById(id);
        if (!optional.isPresent()){
            throw new DataException(ExceptionEnum.DATA_NOT_EXIST);
        }

        T data = optional.get();
        data.setStatus(DataStatusEnum.INVALID.getCode());

        T result = baseRespository.save((T)data);

        return result!=null && result.getStatus().equals(DataStatusEnum.INVALID.getCode());
    }
    public boolean removeByIds(List<Long> ids){
        List<T> datas = baseRespository.findAllById(ids);

        Optional.ofNullable(datas).ifPresent(data ->data.stream().forEach( d -> {
            d.setStatus(DataStatusEnum.INVALID.getCode());
            baseRespository.save(d);
        }));

        return true;
    }
    public List<T>  findBySenstive(T entity){
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withIgnoreNullValues();
        Example<T> example = Example.of(entity,exampleMatcher);
        return baseRespository.findAll(example);
    }
    public List<T> findAll() {
        return baseRespository.findAll();
    }
    public List<T> queryByCriteria(IQueryCriteria queryCriteria, T entity){
        return baseRespository.findAll(SpecificationBuilder.of(entity).build(queryCriteria));
    }

    public Page<T> queryByCriteria(IQueryCriteria queryCriteria,T entity,Page<?> page){
        PageRequest pageRequest = PageRequest.of(page.getCurrentPage(),page.getPageSize());
        org.springframework.data.domain.Page<T> result = baseRespository.findAll(SpecificationBuilder.of(entity).build(queryCriteria),pageRequest);

        Page<T> data = Page.of(result.getContent());
        data.setCurrentPage(page.getCurrentPage());
        data.setTotalPage(result.getTotalPages());
        data.setTotalSize(result.getTotalElements());
        data.setPageSize(page.getPageSize());
        return data;
    }
}
