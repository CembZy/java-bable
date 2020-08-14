package cn.enjoyedu.service;

import cn.enjoyedu.rpc.DepotService;
import cn.enjoyedu.rpc.RpcProxy;
import cn.enjoyedu.vo.GoodTransferVo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;

/**
 *@author Mark老师   享学课堂 https://enjoy.ke.qq.com
 *往期视频咨询芊芊老师  QQ：2130753077  VIP课程咨询 依娜老师  QQ：2470523467
 *类说明：
 */
@Service
@Qualifier("rpc")
public class RpcMode implements IProDepot {

    private static final String IP = "127.0.0.1";
    private static final int PORT = 10002;

    public void processDepot(String goodsId, int amount) {

        DepotService service = RpcProxy.getRmoteProxyObj(DepotService.class,
                new InetSocketAddress(IP,PORT));
        GoodTransferVo goodTransferVo = new GoodTransferVo();
        goodTransferVo.setGoodsId(goodsId);
        goodTransferVo.setChangeAmount(amount);
        goodTransferVo.setInOrOut(true);
        service.changeDepot(goodTransferVo);
    }
}
