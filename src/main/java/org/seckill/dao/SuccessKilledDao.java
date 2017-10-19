package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.SuccessKilled;

/**
 * 秒杀明细dao层接口
 */
public interface SuccessKilledDao {
    /**
     * 插入购买明细
     * @param seckillId id
     * @param userPhone 用户手机号
     * @return 插入的行数
     * 返回0表示插入失败
     */
    int insertSuccessKilled(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);

    /**
     * 根据id查询SuccessKilled并携带秒杀商品实体
     * @param seckillId id
     * @return successKilled
     */
    SuccessKilled queryByIdWithSeckill(@Param("seckillId") long seckillId,@Param("userPhone") long userPhone);
}
