package com.example.lab08.repository;

import com.example.lab08.model.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDetailRepository
        extends JpaRepository<ProductDetail, Long> {
}
