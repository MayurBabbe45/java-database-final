package com.project.code.Controller;

import com.project.code.Model.Product;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Repo.OrderItemRepository;
import com.project.code.Repo.ProductRepository;
import com.project.code.Service.ServiceClass;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ServiceClass serviceClass;

    @Autowired
    private InventoryRepository inventoryRepository;

    @PostMapping
    public Map<String, String> addProduct(@RequestBody Product product) {
        Map<String, String> response = new HashMap<>();
        try {
            if (!serviceClass.validateProduct(product)) {
                response.put("message", "Product already exists");
                return response;
            }
            productRepository.save(product);
            response.put("message", "Product created successfully");
        } catch (DataIntegrityViolationException e) {
            response.put("message", "Data integrity error: " + e.getMessage());
        } catch (Exception e) {
            response.put("message", "Error creating product: " + e.getMessage());
        }
        return response;
    }

    @GetMapping("/{id}")
    public Map<String, Object> getProductbyId(@PathVariable("id") Long id) {
        Map<String, Object> response = new HashMap<>();
        Product product = productRepository.findByid(id);
        response.put("products", product);
        return response;
    }

    @PutMapping
    public Map<String, String> updateProduct(@RequestBody Product product) {
        Map<String, String> response = new HashMap<>();
        try {
            productRepository.save(product);
            response.put("message", "Product updated successfully");
        } catch (DataIntegrityViolationException e) {
            response.put("message", "Data integrity error: " + e.getMessage());
        } catch (Exception e) {
            response.put("message", "Error updating product: " + e.getMessage());
        }
        return response;
    }

    @GetMapping("/category/{name}/{category}")
    public Map<String, Object> filterbyCategoryProduct(@PathVariable("name") String name,
            @PathVariable("category") String category) {
        Map<String, Object> response = new HashMap<>();
        List<Product> products;
        if ("null".equalsIgnoreCase(name) && "null".equalsIgnoreCase(category)) {
            products = productRepository.findAll();
        } else if ("null".equalsIgnoreCase(name)) {
            products = productRepository.findByCategory(category);
        } else if ("null".equalsIgnoreCase(category)) {
            products = productRepository.findProductBySubName(name);
        } else {
            products = productRepository.findProductBySubNameAndCategory(name, category);
        }
        response.put("products", products);
        return response;
    }

    @GetMapping
    public Map<String, Object> listProduct() {
        Map<String, Object> response = new HashMap<>();
        response.put("products", productRepository.findAll());
        return response;
    }

    @GetMapping("filter/{category}/{storeid}")
    public Map<String, Object> getProductbyCategoryAndStoreId(@PathVariable("category") String category,
            @PathVariable("storeid") Long storeid) {
        Map<String, Object> response = new HashMap<>();
        response.put("product", productRepository.findProductByCategory(category, storeid));
        return response;
    }

    @DeleteMapping("/{id}")
    public Map<String, String> deleteProduct(@PathVariable("id") Long id) {
        Map<String, String> response = new HashMap<>();
        if (!serviceClass.ValidateProductId(id)) {
            response.put("message", "Product not present in database");
            return response;
        }
        orderItemRepository.deleteByProductId(id);
        inventoryRepository.deleteByProductId(id);
        productRepository.deleteById(id);
        response.put("message", "Product deleted successfully");
        return response;
    }

    @GetMapping("/searchProduct/{name}")
    public Map<String, Object> searchProduct(@PathVariable("name") String name) {
        Map<String, Object> response = new HashMap<>();
        response.put("products", productRepository.findProductBySubName(name));
        return response;
    }
}
