package com.concurrent.ch03.rw;

/**
 * 类说明：商品的服务的接口
 */
public interface GoodsService {

    GoodsInfo getNum();//获得商品的信息

    void setNum(int number);//设置商品的数量
}
