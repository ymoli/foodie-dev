package com.lank.pojo.vo;

public class OrderVO {
    private String orderId;
    private MerchantOrdersVO merchantOrdersVo;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public MerchantOrdersVO getMerchantOrdersVo() {
        return merchantOrdersVo;
    }

    public void setMerchantOrdersVo(MerchantOrdersVO merchantOrdersVo) {
        this.merchantOrdersVo = merchantOrdersVo;
    }
}
