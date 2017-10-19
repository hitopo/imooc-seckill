package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.Seckill;

import java.util.Date;
import java.util.List;

/**
 * 秒杀接口
 */
public interface SeckillDao {
    /**
     * 减库存
     * @param seckillId id
     * @param killTime 秒杀时间
     * @return 更新行数
     * 如果影响行数>1，表示更新的行数
     */
    int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);

    /**
     * 根据id查询秒杀对象
     *
     * @param seckillId id
     * @return 秒杀对象
     */
    Seckill queryById(long seckillId);

    /**
     * 根据偏移量查询商品列表
     * @param offset 开始量
     * @param limit 总数目
     * @return 商品列表
     */
    List<Seckill> queryAll(@Param("offset") int offset, @Param("limit") int limit);
}
