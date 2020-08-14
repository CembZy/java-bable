package com.proxy.proxy;

import com.proxy.proxy.server.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @ClassName: Runner
 * @Auther: CemB
 * @Description:
 * @Email: 729943791@qq.com
 * @Company: 东方瑞云
 * @Date: 2018/12/20 15:45
 */
@Component
public class Runner implements ApplicationRunner {

    @Value("${websync.port}")
    private int port;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        new Server(port).start();
    }

}


