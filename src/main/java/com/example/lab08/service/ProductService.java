package com.example.lab08.service;

import com.example.lab08.model.Product;
import com.example.lab08.repository.ProductRepository;
import com.example.lab08.strategy.DiscountContext;
import com.example.lab08.strategy.DiscountStrategy;
import com.example.lab08.strategy.MemberDiscountStrategy;
import com.example.lab08.strategy.NoDiscountStrategy;
import com.example.lab08.strategy.SeasonalSaleStrategy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll() {
    List<Product> products = productRepository.findAll();

    for (Product product : products) {
        product.setDiscountedPrice(calculateFinalPrice(product));
    }

    return products;
}

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Product not found: " + id));
    }

    public Product save(Product product) {

        if (product.getDetail() != null) {
            product.getDetail().setProduct(product);
        }

        return productRepository.save(product);
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    public double calculateFinalPrice(Product product) {

        DiscountStrategy strategy;

        if (product.getDiscountType() == null) {
            strategy = new NoDiscountStrategy();

        } else if (product.getDiscountType()
                .equalsIgnoreCase("MEMBER")) {

            strategy = new MemberDiscountStrategy();

        } else if (product.getDiscountType()
                .equalsIgnoreCase("SEASONAL")) {

            strategy = new SeasonalSaleStrategy();

        } else {

            strategy = new NoDiscountStrategy();
        }

        DiscountContext context =
                new DiscountContext(strategy);

        return context.calculatePrice(product.getPrice());
    }
}