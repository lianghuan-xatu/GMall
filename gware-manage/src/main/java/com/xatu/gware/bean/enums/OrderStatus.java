package com.xatu.gware.bean.enums;

/**
 * @param
 * @return
 */
public enum OrderStatus {
       UNPAID("未支付"),
       PAID("已支付" ),
       WAITING_DELEVER("待发货"),
       DELEVERED("已发货"),
       CLOSED("已关闭"),
       FINISHED("已完结") ,
       SPLIT("订单已拆分");

    private String comment ;


    OrderStatus(String comment ){
        this.comment=comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


}
