package br.edu.ifmg.produto.util;

import br.edu.ifmg.produto.dtos.CategoryDTO;
import br.edu.ifmg.produto.dtos.ProductDTO;
import br.edu.ifmg.produto.entities.Category;
import br.edu.ifmg.produto.entities.Product;

import java.math.BigDecimal;

public class Factory {

    public static Product createProduct() {
        Product p = new Product();
        p.setName("IPhone XXX");
        p.setPrice(new BigDecimal("199.99"));
        p.setImageUrl("https://img.com/iphonexxx.jpg");
        p.getCategories().add(new Category());
        return p;
    }

    public static ProductDTO createProductDTO() {
        Product p = createProduct();
        return new ProductDTO(p);
    }

    public static Category createCategory() {
        Category c = new Category();
        c.setName("IPhone XXX");
        c.setImageUrl("https://img.com/iphonexxx.jpg");
        return c;
    }

    public static CategoryDTO createCategoryDTO() {
        Category c = createCategory();
        return new CategoryDTO(c);
    }

}
