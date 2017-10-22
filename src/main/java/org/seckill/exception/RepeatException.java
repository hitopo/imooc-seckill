package org.seckill.exception;

/**
 * 重复秒杀异常
 * 每一个商品和手机号只能秒杀一次
 * 防止用户多次秒杀带来的不公平以及服务器负载过大
 */
public class RepeatException extends SeckillException {

    public RepeatException(String message) {
        super(message);
    }

    public RepeatException(String message, Throwable cause) {
        super(message, cause);
    }
}
