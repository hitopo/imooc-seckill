package org.seckill.enums;

/**
 * 秒杀状态码和秒杀状态信息的对应关系
 * 数据字典
 */
public enum SeckillStateEnum {
    SUCCESS(1, "秒杀成功"),
    END(0, "秒杀结束"),
    REPEAT_KILL(-1, "重复秒杀"),
    INNER_ERROR(-2, "系统异常"),
    DATA_REWRITE(-3, "数据篡改");

    //状态码
    private int state;
    //状态信息
    private String stateInfo;


    /**
     * 根据状态码获取状态信息
     * @return 状态枚举
     */
    public static SeckillStateEnum stateOf(int index) {
        //穷举法获取枚举对象
        for (SeckillStateEnum seckillStateEnum : values()) {
            if(seckillStateEnum.getState() == index) {
                return seckillStateEnum;
            }
        }
        return null;
    }


    SeckillStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }



    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }
}
