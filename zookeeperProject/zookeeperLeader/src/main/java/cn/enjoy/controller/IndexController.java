package cn.enjoy.controller;

import cn.enjoy.listener.ElectionMaster;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {
    // 获取服务信息
    @RequestMapping("/getServerInfo")
    public String getServerInfo() {
        return ElectionMaster.isSurvival ? "当前服务器为主节点" : "当前服务器为从节点";
    }
}
