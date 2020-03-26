package com.ai.spring.boot.content.dao;

import com.ai.spring.boot.content.dao.bean.Share;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * 分享操作类
 *
 * @author 石头
 * @Date 2020/3/25
 * @Version 1.0
 **/
@Repository
public class ShareDao {
    private static final Map<Long ,Share> cachedShares = new HashMap<>(8);
    static {
        cachedShares.put(1L,Share.builder().userId(1L).shareId(1L).price(1).author("小白").title("NBA").isOriginal(true));
        cachedShares.put(2L,Share.builder().userId(1L).shareId(2L).price(2).author("小白").title("CBA").isOriginal(true));
        cachedShares.put(3L,Share.builder().userId(3L).shareId(3L).price(2).author("梅子").title("NEWS").isOriginal(true));
        cachedShares.put(4L,Share.builder().userId(3L).shareId(4L).price(1).author("梅子").title("孙杨事件").isOriginal(true));
    }
    public Share selectByPrimaryKey(Long shareId){
        return cachedShares.get(shareId);
    }
}
