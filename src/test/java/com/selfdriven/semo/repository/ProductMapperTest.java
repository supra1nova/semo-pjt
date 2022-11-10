package com.selfdriven.semo.repository;

import com.selfdriven.semo.entity.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductMapperTest {

    @Autowired
    private ProductMapper productMapper;

    @Test
    public void 업체생성테스트() {
        productMapper.deleteAllProduct();
        Product product = new Product("2465994341", "테스트업체1", "h", "테스트주소", "테스트상세주소", "111222", "0211112222");
        productMapper.addProduct(product);
        assertThat(productMapper.getProductList().size()).isEqualTo(1);
    }

    @Test
    public void 업체조회테스트(){
        productMapper.deleteAllProduct();
        Product product = new Product("2465994341", "테스트업체1", "h", "테스트주소", "테스트상세주소", "111222", "0211112222");
        productMapper.addProduct(product);
        List<Product> pList = productMapper.getProductList();
        Product foundProduct = productMapper.getProductById(pList.get(0).getProductId());
        assertThat(foundProduct.getMemberId()).isEqualTo(product.getMemberId());
    }

    @Test
    public void 전체업체조회테스트() {
        productMapper.deleteAllProduct();
        int num = 6;
        for (int i = 1; i < num; i++) {
            Product product = new Product("2465994341", "테스트업체" + i, "h", "테스트주소 " + i, "테스트상세주소 " + i, "11122" + i, "021111222" + i);
            productMapper.addProduct(product);
        }
        List<Product> productList = productMapper.getProductList();
        assertThat(productList.size()).isEqualTo(num - 1);
    }

}