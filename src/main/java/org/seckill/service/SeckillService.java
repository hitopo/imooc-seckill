package org.seckill.service;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;

import java.util.List;

/**
 * 秒杀业务层接口
 */
public interface SeckillService {
    /**
     * 获取商品信息
     *
     * @param seckillId id
     * @return 商品
     */
    Seckill getById(long seckillId);

    /**
     * 获取全部商品信息
     *
     * @return 商品列表
     */
    List<Seckill> getSeckillList();


    /**
     * 获取秒杀接口
     * @param seckillId id
     * @return dto exporter对象 传递秒杀接口url信息
     */
    Exposer exportSeckillUrl(long seckillId);


    /**
     * 执行秒杀操作
     * @param seckillId id
     * @param userPhone 用户数手机号
     * @return 传递给前端的执行结果
     */
    SeckillExecution executeSeckill(long seckillId, long userPhone,String md5)
    throws SeckillException,RepeatException,SeckillCloseException;
}
