// Import Statements
package com.onlineshop.controller;

import com.onlineshop.entity.Product;
import com.onlineshop.entity.Brand;
import com.onlineshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

// Class Definition
@Controller
@RequestMapping("/manageProduct")
public class ProductManagerController {

    @Autowired
    private ProductService productService;

    // List All Products
    @GetMapping("/listAllProducts")
    public String listAllProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("showSearchProduct", "Yes");
        model.addAttribute("allProducts", products);
        return "ProductManager";
    }

    // Search Products by Keywords
    @GetMapping("/searchByKeywords")
    public String searchProducts(@RequestParam("keywords") String keywords, Model model) {
        List<Product> products = productService.getProductsByName(keywords);
        model.addAttribute("keywords", keywords);
        model.addAttribute("showSearchProduct", "Yes");
        if (products == null || products.isEmpty()) {
            model.addAttribute("notFoundProduct", "Your keywords do not match with any Product Name");
            products = productService.getAllProducts();
        }
        model.addAttribute("allProducts", products);
        return "ProductManager";
    }

    // Request Update Product
    @GetMapping("/requestUpdate")
    public String requestUpdate(@RequestParam("productId") Integer productId, Model model) {
        Product product = productService.getProductById(productId);
        if (product == null) {
            model.addAttribute("error", "Product not found for ID " + productId);
            return "errorPage";  // Replace "errorPage" with your error view name
        }
        model.addAttribute("productUpdate", product);
        return "ProductManager";
    }


    // Send Update Product Details
    @GetMapping("/sendUpdateDetail")
    public String sendUpdateDetail(@RequestParam("id") Integer id,
                                   @RequestParam("name") String name,
                                   @RequestParam("price") double price,
                                   @RequestParam("quantity") int quantity,
                                   @RequestParam("release_date") String releaseDate,
                                   RedirectAttributes redirectAttributes) {
        productService.updateProduct(id, name, price, quantity, LocalDate.parse(releaseDate));
        redirectAttributes.addFlashAttribute("UpdateDone", "Update information for Product (ID = " + id + ") done! Click Product Manager to see all changes");
        return "redirect:/manageProduct/listAllProducts";
    }


    // Request Insert New Product
    @GetMapping("/requestInsert")
    public String requestInsert(Model model) {
        List<Brand> brands = productService.getAllBrands();
        model.addAttribute("insertProduct", "insertProduct");
        model.addAttribute("allBrands", brands);
        return "ProductManager";
    }

    // Send Insert Product Details
    @GetMapping("/sendInsertDetail")
    public String sendInsertDetail(@RequestParam("name") String name,
                                   @RequestParam("price") double price,
                                   @RequestParam("quantity") int quantity,
                                   @RequestParam("description") String description,
                                   @RequestParam("image_url") String imageUrl,
                                   @RequestParam("brand") int brandId,
                                   @RequestParam("release_date") String releaseDate, Model model) {
        productService.insertProduct(name, price, quantity, description, imageUrl, brandId, LocalDate.parse(releaseDate));
        model.addAttribute("InsertDone", "Insert a new Product done! Click Product Manager to see all changes");
        return "redirect:/manageProduct/listAllProducts";
    }

    // Request Delete Product
    @GetMapping("/requestDelete")
    public String requestDelete(@RequestParam("productId") Integer productId, Model model) {
        boolean deleted = productService.deleteProduct(productId);
        if (deleted) {
            model.addAttribute("deleteDone", "Delete Product (ID = " + productId + ") done!");
        } else {
            model.addAttribute("deleteDone", "Failed to delete Product (ID = " + productId + ") because it is associated with an order.");
        }
        return "redirect:/manageProduct/listAllProducts";
    }
}
