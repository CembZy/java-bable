package cn.enjoy.service;
import cn.enjoy.vo.Product;
import java.util.List;
public interface IProductService {
    Product get(long id);
    boolean add(Product product);
    List<Product> list();
}