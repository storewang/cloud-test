package com.ai.spring.boot.flux.datasource.jpa;

import com.ai.spring.boot.flux.dao.bean.BaseEntity;
import com.ai.spring.boot.flux.datasource.annotation.OrderBy;
import com.ai.spring.boot.flux.datasource.annotation.Query;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Predicate创建者
 *
 * @author 石头
 * @Date 2020/6/5
 * @Version 1.0
 **/
public class SpecificationBuilder<T extends BaseEntity> {
    private T data;
    private SpecificationBuilder(T data){
        this.data = data;
    }
    /**获取创建者对象*/
    public static  <E extends BaseEntity> SpecificationBuilder<E>  of(E entity){
        return new SpecificationBuilder(entity);
    }

    public Specification<T> build(IQueryCriteria query){
        Specification<T> spec = (root, criteriaQuery, criteriaBuilder) ->{
            List<Predicate> list = new ArrayList<>();
            // 处理实体类属性
            doWithFields(list,root,criteriaBuilder);
            // 处理扩展查询属性
            doWithQueryCriteria(list,query,root,criteriaBuilder);
            // 加入where条件
            if (!CollectionUtils.isEmpty(list)){
                criteriaQuery.where(list.toArray(new Predicate[list.size()]));
            }
            // 加入分组
            doWithQueryGroupBy(criteriaQuery,query,root);
            // 加入排序
            doWithQueryOrderBy(criteriaQuery,query,root,criteriaBuilder);

            return criteriaQuery.getRestriction();
        };

        return spec;
    }

    private void doWithQueryOrderBy(CriteriaQuery<?> criteriaQuery, IQueryCriteria query, Root<T> root, CriteriaBuilder cb){
        if (query==null){
            return;
        }
        ReflectionUtils.doWithFields(query.getClass(), (field) -> {
            ReflectionUtils.makeAccessible(field);
            OrderBy q = field.getAnnotation(OrderBy.class);
            if (q != null) {
                OrderBy.Type orderType = q.type();

                Object fieldVal    = field.get(query);
                String   propName  = q.propName();
                if (fieldVal == null && StringUtils.isEmpty(propName)){
                    return;
                }
                if (fieldVal!=null){
                    propName = fieldVal.toString();
                }

                // 获取order by 相对应属性的类型（从entity中获取属性）
                Field dataField = ReflectionUtils.findField(root.getJavaType(),propName);
                ReflectionUtils.makeAccessible(dataField);
                Class<?> fieldType = dataField.getType();

                if (orderType == OrderBy.Type.ASC){
                    criteriaQuery.orderBy(cb.asc(root.get(propName).as(fieldType)));
                }else {
                    criteriaQuery.orderBy(cb.desc(root.get(propName).as(fieldType)));
                }
            }
        });
    }
    private void doWithQueryGroupBy(CriteriaQuery<?> criteriaQuery, IQueryCriteria query, Root<T> root){
        if (query == null || StringUtils.isEmpty(query.groupBy())){
            return;
        }
        String groupBy = query.groupBy();
        String[] groupNames = StringUtils.tokenizeToStringArray(groupBy,",");
        List<Expression<?>> expressions = new ArrayList<>();
        Arrays.stream(groupNames).forEach(groupName ->{
            Field field = ReflectionUtils.findField(data.getClass(),groupName);
            ReflectionUtils.makeAccessible(field);
            Class<?> fieldType = field.getType();

            expressions.add(root.get(groupName).as((Class<? extends Comparable>) fieldType));
        });
        if (!CollectionUtils.isEmpty(expressions)){
            criteriaQuery.groupBy(expressions);
        }
    }
    private void doWithQueryCriteria(List<Predicate> list,IQueryCriteria query,Root<T> root,CriteriaBuilder cb){
        if (query==null){
            return;
        }
        ReflectionUtils.doWithFields(query.getClass(), (field) -> {
            ReflectionUtils.makeAccessible(field);

            Query q = field.getAnnotation(Query.class);
            Class<?> fieldType = field.getType();
            Object fieldVal    = field.get(query);
            if (q == null || fieldVal == null){
                return;
            }

            String propName      = q.propName();
            Query.Type queryType = q.type();
            if (propName == null || propName.length()<1){
                propName    = field.getName();
            }

            CriteriaBuilderFactory.buildCriteria(queryType,list,root,cb,fieldType,fieldVal,propName);
        });
    }
    /**
     * 处理实体类的属性，不为空都将使用eq
     * @param list
     * @param root
     * @param cb
     */
    private void doWithFields(List<Predicate> list, Root<T> root, CriteriaBuilder cb){
        if (data==null){
            return;
        }
        ReflectionUtils.doWithFields(data.getClass(), (field) -> {
            ReflectionUtils.makeAccessible(field);

            Object fieldVal = field.get(data);
            if (fieldVal!=null){
                Class<?> fieldType = field.getType();
                String attributeName = field.getName();

                CriteriaBuilderFactory.buildCriteria(Query.Type.EQ,list,root,cb,fieldType,fieldVal,attributeName);
            }
        });
    }
}
