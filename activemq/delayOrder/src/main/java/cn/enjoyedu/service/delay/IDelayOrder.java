package cn.enjoyedu.service.delay;

import cn.enjoyedu.model.OrderExp;

/**
 *
 *类说明：延时处理订单的的接口
 */
public interface IDelayOrder {

	/**
	 * 进行延时处理的方法
	 * @param order 要进行延时处理的订单
	 * @param expireTime 延时时长，单位秒
	 */
    public void orderDelay(OrderExp order, long expireTime);

}
