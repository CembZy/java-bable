package cn.enjoy.test;

import cn.enjoy.simple.OrderNumGenerator;
import cn.enjoy.zk.Lock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * Created by VULCAN on 2018/9/20.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {cn.enjoy.App.class})
public class MySqlTest {
    private OrderNumGenerator orderNumGenerator = new OrderNumGenerator();
    // 使用lock锁
    // private java.util.concurrent.locks.Lock lock = new ReentrantLock();


    @Resource
    private Lock lock;



    @Test
    public  void testGetOrderNumber() throws InterruptedException {
        System.out.println("####生成唯一订单号###");
        for (int i = 0; i < 50; i++) {
            new Thread(
                    new Runnable() {
                        public void run() {
                            getNumber();
                        }
                    }
            ).start();
        }

        Thread.currentThread().join();
    }


    public void getNumber() {
        try {
            lock.getLock();
            String number = orderNumGenerator.getNumber();
            System.out.println(Thread.currentThread().getName() + ",生成订单ID:" + number);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unLock();
        }
    }
}