
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.james.cache.services.ShopService;
import org.junit.runners.MethodSorters;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-mvc.xml")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CartShopTest {

	@Resource(name = "shopServiceImpl")
	private ShopService shopService;

	@Test
	public void asLoginTest() throws InterruptedException {
		
		 
		String username = "james";
		String itemCode = null; // 登录时未浏览商品
		System.out.println("登录成功---------");
		// 模拟服务器生成一个令牌token
		String token = UUID.randomUUID().toString();
		
		// 更新token
		shopService.updateToken(token, username, itemCode);
		System.out.println("刚刚更新的token令牌为: " + token+"; 当前操作的用户: "+username);
		
		String user = shopService.checkToken(token);// 尝试获取并返回令牌token对应的用户
		System.out.println(user);
		
		//考虑到redis的存储,使用线程对超出cookie个数的旧cookie从redis清除
		//如果token令牌的数量超过3了，只保留3个tokenid
		shopService.removeOldTokens(3);
		long s = shopService.hlen("login:info");
		System.out.println("查看当前的cookie剩余个数: " + s);
	}
	
	@Test
	public void putToCart() throws InterruptedException {
		//商品加入购物车及购物车商品列表查询
		String username = "james";
		String itemCode = "xmPhone"; // 浏览的商品为Vivo手机
		String token = UUID.randomUUID().toString();

		System.out.println("开始刷新session...");

		// 更新token,因为此用户登录进来,之前的旧token可能失效......
		shopService.updateToken(token, username, itemCode);
	    //假设将vivoPhone  数量为3, 加入购物车
		long i =shopService.addToCart(token, itemCode, 3);
		if(i >0 ){
			System.out.println("加入购物车成功");
		}else{
			System.out.println("加入购物车失败");
		}
		//查看用户james的购物车有哪些商品
		Map<String, String> r = shopService.hgetAll("cart:" + token);
		System.out.println("用户 " + username +" 的购物车里有如下商品:");
		for (Map.Entry<String, String> entry : r.entrySet()) {
			System.out.println("  " + entry.getKey() + ": " + entry.getValue());
		}
	}

}