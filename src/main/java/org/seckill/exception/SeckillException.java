package org.seckill.exception;

/**
 * 系统异常
 * 其他异常归于这一类（md5篡改等等）
 */
public class SeckillException extends RuntimeException {
    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
