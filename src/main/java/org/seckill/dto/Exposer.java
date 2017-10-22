package org.seckill.dto;

import java.util.Date;

/**
 * 获取秒杀接口地址返回给前端的封装
 */
public class Exposer {
    //商品id
    private long seckillId;
    //是否开始秒杀（是否可以进行秒杀操作）
    private boolean exposed;
    //服务器端生成的md5
    private String md5;
    //秒杀开始时间
    private long start;
    //秒杀结束时间
    private long end;
    //当前服务器时间
    private long now;

    //可以秒杀时返回的信息，主要是返回md5
    public Exposer(long seckillId, boolean exposed, String md5) {
        this.seckillId = seckillId;
        this.exposed = exposed;
        this.md5 = md5;
    }

    //不在秒杀时段返回的信息（start、now、end是为了前端方便倒计时）
    public Exposer(long seckillId, boolean exposed, long start, long end, long now) {
        this.seckillId = seckillId;
        this.exposed = exposed;
        this.start = start;
        this.end = end;
        this.now = now;
    }

    public Exposer(long seckillId, boolean exposed) {
        this.seckillId = seckillId;
        this.exposed = exposed;
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public boolean isExposed() {
        return exposed;
    }

    public void setExposed(boolean exposed) {
        this.exposed = exposed;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public long getNow() {
        return now;
    }

    public void setNow(long now) {
        this.now = now;
    }

    @Override
    public String toString() {
        return "Exposer{" +
                "seckillId=" + seckillId +
                ", exposed=" + exposed +
                ", md5='" + md5 + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", now=" + now +
                '}';
    }
}
