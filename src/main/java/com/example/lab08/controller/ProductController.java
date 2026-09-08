package com.example.lab08.controller;


import com.example.lab08.model.Product;
import com.example.lab08.model.ProductDetail;
import com.example.lab08.model.Review;
import com.example.lab08.service.ProductService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    // Constructor Injection
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // =========================
    // READ
    // GET /products
    // =========================

    @GetMapping
    public String list(Model model) {

        model.addAttribute(
                "products",
                productService.findAll()
        );

        return "products/list";
    }

    // =========================
    // ADD FORM
    // GET /products/add
    // =========================

    @GetMapping("/add")
    public String add(Model model) {

        Product product = new Product();

        // สร้าง ProductDetail สำหรับ 1:1
        product.setDetail(new ProductDetail());

        // สร้าง Review ตัวแรกสำหรับ reviews[0]
        product.getReviews().add(new Review());

        model.addAttribute("product", product);

        return "products/add";
    }

    // =========================
    // SAVE
    // POST /products/save
    // =========================

    @PostMapping("/save")
    public String save(@ModelAttribute Product product) {

        productService.save(product);

        return "redirect:/products";
    }

    // =========================
    // EDIT FORM
    // GET /products/edit/{id}
    // =========================

    @GetMapping("/edit/{id}")
    public String edit(
            @PathVariable Long id,
            Model model
    ) {

        Product product =
                productService.findById(id);

        model.addAttribute(
                "product",
                product
        );

        return "products/edit";
    }

    // =========================
    // UPDATE
    // POST /products/update/{id}
    // =========================

    @PostMapping("/update/{id}")
    public String update(
            @PathVariable Long id,
            @ModelAttribute Product product
    ) {

        productService.update(id, product);

        return "redirect:/products";
    }

    // =========================
    // DELETE PAGE
    // GET /products/delete/{id}
    // =========================

    @GetMapping("/delete/{id}")
    public String deletePage(
            @PathVariable Long id,
            Model model
    ) {

        Product product =
                productService.findById(id);

        model.addAttribute(
                "product",
                product
        );

        return "products/delete";
    }

    // =========================
    // DELETE
    // POST /products/delete/{id}
    // =========================

    @PostMapping("/delete/{id}")
    public String delete(
            @PathVariable Long id
    ) {

        productService.delete(id);

        return "redirect:/products";
    }
}