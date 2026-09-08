package com.example.lab08.controller;

import com.example.lab08.model.Product;
import com.example.lab08.model.ProductDetail;
import com.example.lab08.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // READ
    @GetMapping
    public String list(Model model) {

        model.addAttribute(
                "products",
                productService.findAll()
        );

        return "products/list";
    }

    // CREATE
    @GetMapping("/add")
    public String add(Model model) {

        Product product = new Product();
        product.setDetail(new ProductDetail());

        model.addAttribute("product", product);

        return "products/add";
    }

    // CREATE / UPDATE
    @PostMapping("/save")
    public String save(@ModelAttribute Product product) {

        productService.save(product);

        return "redirect:/products";
    }

    // UPDATE
    @GetMapping("/edit/{id}")
    public String edit(
            @PathVariable Long id,
            Model model) {

        Product product = productService.findById(id);

        model.addAttribute("product", product);

        return "products/edit";
    }

    // DELETE confirmation
    @GetMapping("/delete/{id}")
    public String deletePage(
            @PathVariable Long id,
            Model model) {

        Product product = productService.findById(id);

        model.addAttribute("product", product);

        return "products/delete";
    }

    // DELETE
    @PostMapping("/delete/{id}")
    public String delete(
            @PathVariable Long id) {

        productService.delete(id);

        return "redirect:/products";
    }
}
