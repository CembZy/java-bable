package cn.enjoy.order.pojo;

/**
 * Created by VULCAN on 2018/7/25.
 */
public class Order {
    private  String id;
    private  String orderName;
    private Product product;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Order(String id, String orderName, Product product) {
        this.id = id;
        this.orderName = orderName;
        this.product = product;
    }

    public Order() {
    }
}
