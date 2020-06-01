package com.xatu.gware.bean.enums;

/**
 * @param
 * @return
 */
public enum PaymentWay {
       ONLINE("在线支付"),
       OUTLINE("货到付款" );

    private String comment ;


    PaymentWay(String comment ){
        this.comment=comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


}
