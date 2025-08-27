package com.example.demo.repository;

import com.example.demo.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @Test
    void sav_and_findBySku() {
        var p = new Product();
        p.setName("Phone");
        p.setSku("P-001");
        p.setPrice(new BigDecimal("999.99"));
        p.setStock(10);

        productRepository.save(p);
        var found = productRepository.findBySku("P-001");
        assertThat(found).isPresent();
        assertThat(found.get().getPrice()).isEqualByComparingTo("999.99");
    }
}
