package org.seckill.service.impl;

import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * SeckillService接口实现类
 */
@Service
public class SeckillServiceImpl implements SeckillService {

    //随机书写的扰乱码
    private final String slat = "asfkhjk@$R*sfy8S^&&*^&*^SD^&%^DS%as9";
    //日志输出
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SeckillDao seckillDao;
    @Autowired
    private SuccessKilledDao successKilledDao;

    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0, 4);
    }

    public Exposer exportSeckillUrl(long seckillId) {
        //查询商品
        Seckill seckill = seckillDao.queryById(seckillId);
        if (seckill == null) {
            //没有查询到该商品
            return new Exposer(seckillId, false);
        }
        //存在该商品
        //查询时间是否在秒杀时段内
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        //当前系统时间
        Date now = new Date();
        if (startTime.getTime() > now.getTime()
                || endTime.getTime() < now.getTime()) {
            //不在秒杀时间段内
            //返回秒杀开始时间，结束时间，当前服务器时间
            return new Exposer(seckillId, false, startTime.getTime(), endTime.getTime(), now.getTime());
        }
        String md5 = getMd5(seckillId);

        //秒杀接口暴露
        //返回随机生成的md5
        return new Exposer(seckillId, true, md5);
    }


    //该方法抛出异常是为了上层可以捕获异常处理
    //spring声明式事务管理只可以回滚RunTimeException
    @Transactional
    /**
     * 使用注解控制事务方法的优点:
     * 1.开发团队达成一致约定，明确标注事务方法的编程风格
     * 2.保证事务方法的执行时间尽可能短，不要穿插其他网络操作RPC/HTTP请求或者剥离到事务方法外部
     * 3.不是所有的方法都需要事务，如只有一条修改操作、只读操作不要事务控制
     */
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatException, SeckillCloseException {
        if (md5 == null || !getMd5(seckillId).equals(md5)) {
            //前端传递的md5为空或者和服务器端再次生成的md5不相同，说明信息被篡改了
            return new SeckillExecution(seckillId, SeckillStateEnum.DATA_REWRITE);
        }
        //获取服务器时间
        Date nowTime = new Date();
        //执行秒杀逻辑
        //减少库存 + 插入秒杀信息
        try {
            //减少库存
            int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
            if (updateCount <= 0) {
                //更新行数小于等于0，表示更新失败
                //秒杀关闭
                throw new SeckillCloseException("秒杀关闭");
            }
            //插入秒杀信息
            int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
            if (insertCount <= 0) {
                //插入行数小于等于0，说明重复秒杀
                throw new RepeatException("重复秒杀");
            }
            //秒杀成功
            //获取秒杀明细并返回
            SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
            return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
        } catch (SeckillCloseException e) {
            throw e;
        } catch (RepeatException e) {
            throw e;
        } catch (Exception e) {
            //将其他异常转换成SeckillException
            logger.info("系统内部异常");
            throw new SeckillException("系统内部异常：" + e.getMessage());
        }
    }


    /**
     * 获取md5
     *
     * @param seckillId id
     * @return 随机生成的md5值
     */
    private String getMd5(long seckillId) {
        String base = seckillId + "/" + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

}
