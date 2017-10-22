package org.seckill.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatException;
import org.seckill.exception.SeckillCloseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml"})
public class SeckillServiceTest {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SeckillService seckillService;

    @Test
    public void getById() throws Exception {
        Seckill seckill = seckillService.getById(1000);
        logger.info("seckill:{}", seckill);
    }

    @Test
    public void getSeckillList() throws Exception {
        List<Seckill> seckills = seckillService.getSeckillList();
        logger.info("seckills:{}", seckills);
    }

    /**
     * 秒杀逻辑测试
     *
     * @throws Exception
     */
    @Test
    public void executeSeckillLogic() throws Exception {
        long seckillId = 1002;
        long userPhone = 13572899721L;
        //获取秒杀接口
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        if (exposer.isExposed()) {
            //开始秒杀
            try {
                SeckillExecution seckillExecution = seckillService.executeSeckill(seckillId, userPhone, exposer.getMd5());
                logger.info("result={}", seckillExecution);
            } catch (RepeatException e) {
                logger.error("重复秒杀");
            } catch (SeckillCloseException e) {
                logger.error("秒杀关闭");
            }
        } else {
            //秒杀未开启
            logger.warn("秒杀未开启");
        }

    }


}