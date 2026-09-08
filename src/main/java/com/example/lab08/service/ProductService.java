package com.example.lab08.service;

import com.example.lab08.model.Product;
import com.example.lab08.model.Review;
import com.example.lab08.repository.ProductRepository;
import com.example.lab08.strategy.*;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    // Constructor Injection
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // =========================
    // READ ALL
    // =========================

    public List<Product> findAll() {

        List<Product> products = productRepository.findAll();

        for (Product product : products) {
            product.setDiscountedPrice(
                    calculateFinalPrice(product)
            );
        }

        return products;
    }

    // =========================
    // READ BY ID
    // =========================

    public Product findById(Long id) {

        return productRepository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Product not found: " + id
                        )
                );
    }

    // =========================
    // CREATE / SAVE
    // =========================

    public Product save(Product product) {

        // ProductDetail
        if (product.getDetail() != null) {
            product.getDetail().setProduct(product);
        }

        // Review
        if (product.getReviews() != null) {

            for (Review review : product.getReviews()) {

                if (review.getReviewer() != null
                        && !review.getReviewer().isBlank()) {

                    review.setProduct(product);

                    if (review.getReviewDate() == null) {
                        review.setReviewDate(LocalDate.now());
                    }
                }
            }
        }

        return productRepository.save(product);
    }

    // =========================
    // UPDATE
    // =========================

    public Product update(Long id, Product updatedProduct) {

        Product existingProduct = findById(id);

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setCategory(updatedProduct.getCategory());
        existingProduct.setBrand(updatedProduct.getBrand());
        existingProduct.setStock(updatedProduct.getStock());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setDiscountType(
                updatedProduct.getDiscountType()
        );

        // Update ProductDetail
        if (existingProduct.getDetail() != null
                && updatedProduct.getDetail() != null) {

            existingProduct.getDetail().setDescription(
                    updatedProduct.getDetail().getDescription()
            );

            existingProduct.getDetail().setWarranty(
                    updatedProduct.getDetail().getWarranty()
            );

            existingProduct.getDetail().setWeight(
                    updatedProduct.getDetail().getWeight()
            );

            existingProduct.getDetail().setDimensions(
                    updatedProduct.getDetail().getDimensions()
            );

            existingProduct.getDetail().setManufacturedCountry(
                    updatedProduct.getDetail().getManufacturedCountry()
            );
        }

        return productRepository.save(existingProduct);
    }

    // =========================
    // DELETE
    // =========================

    public void delete(Long id) {

        productRepository.deleteById(id);
    }

    // =========================
    // STRATEGY PATTERN
    // =========================

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