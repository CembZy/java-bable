package cn.enjoyedu;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CountDownLatch;

@SpringBootTest(classes = KafkaTrafficShapingClient.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class TestInvokeRemote {

    RestTemplate restTemplate = new RestTemplate();

    private static final int num = 200;

    private final String url = "http://127.0.0.1:8090/buyTicket";

    private static CountDownLatch cdl = new CountDownLatch(num);

    @Test
    public void testInvokeRemote() throws InterruptedException {
        for(int i = 0; i <num; i++){
            new Thread(new TicketQuest()).start();
            cdl.countDown(); //0
        }
        Thread.currentThread().sleep(3000);
    }

    // 内部类继承线程接口，用于模拟用户买票请求
    public class TicketQuest implements Runnable{

        @Override
        public void run() {
            try {
                cdl.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String str = restTemplate.getForEntity(url, String.class).getBody();
            System.out.println(str);
        }

    }

}
