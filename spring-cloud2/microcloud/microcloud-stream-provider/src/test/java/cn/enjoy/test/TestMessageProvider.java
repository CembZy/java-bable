package cn.enjoy.test;
import cn.enjoy.StreamProviderApp;
import cn.enjoy.service.IMessageProvider;
import cn.enjoy.vo.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import javax.annotation.Resource;

@SpringBootTest(classes = StreamProviderApp.class)
@RunWith(SpringRunner.class)
public class TestMessageProvider {

    @Resource
    private IMessageProvider messageProvider;

    @Test
    public void testSend() {
        Product product = new Product();
        product.setProductId(1L);
        product.setProductName("messageName");
        product.setProductDesc("desc");
        messageProvider.send(product);
    }
}