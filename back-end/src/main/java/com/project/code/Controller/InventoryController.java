package com.project.code.Controller;

import com.project.code.Model.CombinedRequest;
import com.project.code.Model.Inventory;
import com.project.code.Model.Product;
import com.project.code.Repo.InventoryRepository;
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
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ServiceClass serviceClass;

    @PutMapping
    public Map<String, String> updateInventory(@RequestBody CombinedRequest request) {
        Map<String, String> response = new HashMap<>();
        try {
            if (request == null || request.getProduct() == null || request.getInventory() == null
                    || request.getProduct().getId() == null || request.getInventory().getStore() == null) {
                response.put("message", "Invalid request payload");
                return response;
            }

            if (!serviceClass.ValidateProductId(request.getProduct().getId())) {
                response.put("message", "Product ID is not valid");
                return response;
            }

            Inventory existing = inventoryRepository.findByProductIdandStoreId(request.getProduct().getId(),
                    request.getInventory().getStore().getId());
            if (existing != null) {
                existing.setStockLevel(request.getInventory().getStockLevel());
                existing.setProduct(request.getProduct());
                existing.setStore(request.getInventory().getStore());
                inventoryRepository.save(existing);
                response.put("message", "Successfully updated product");
            } else {
                response.put("message", "No data available");
            }
        } catch (DataIntegrityViolationException e) {
            response.put("message", "Data integrity error: " + e.getMessage());
        } catch (Exception e) {
            response.put("message", "Error updating inventory: " + e.getMessage());
        }
        return response;
    }

    @PostMapping
    public Map<String, String> saveInventory(@RequestBody Inventory inventory) {
        Map<String, String> response = new HashMap<>();
        try {
            if (!serviceClass.validateInventory(inventory)) {
                response.put("message", "Data already present");
                return response;
            }
            inventoryRepository.save(inventory);
            response.put("message", "Data saved successfully");
        } catch (DataIntegrityViolationException e) {
            response.put("message", "Data integrity error: " + e.getMessage());
        } catch (Exception e) {
            response.put("message", "Error saving inventory: " + e.getMessage());
        }
        return response;
    }

    @GetMapping("/{storeid}")
    public Map<String, Object> getAllProducts(@PathVariable("storeid") Long storeid) {
        Map<String, Object> response = new HashMap<>();
        List<Product> products = productRepository.findProductsByStoreId(storeid);
        response.put("products", products);
        return response;
    }

    @GetMapping("filter/{category}/{name}/{storeid}")
    public Map<String, Object> getProductName(@PathVariable("category") String category,
            @PathVariable("name") String name, @PathVariable("storeid") Long storeid) {
        Map<String, Object> response = new HashMap<>();
        List<Product> products;
        if ("null".equalsIgnoreCase(category) && "null".equalsIgnoreCase(name)) {
            products = productRepository.findProductsByStoreId(storeid);
        } else if ("null".equalsIgnoreCase(category)) {
            products = productRepository.findByNameLike(storeid, name);
        } else if ("null".equalsIgnoreCase(name)) {
            products = productRepository.findByCategoryAndStoreId(storeid, category);
        } else {
            products = productRepository.findByNameAndCategory(storeid, name, category);
        }
        response.put("product", products);
        return response;
    }

    @GetMapping("search/{name}/{storeId}")
    public Map<String, Object> searchProduct(@PathVariable("name") String name,
            @PathVariable("storeId") Long storeId) {
        Map<String, Object> response = new HashMap<>();
        List<Product> products = productRepository.findByNameLike(storeId, name);
        response.put("product", products);
        return response;
    }

    @DeleteMapping("/{id}")
    public Map<String, String> removeProduct(@PathVariable("id") Long id) {
        Map<String, String> response = new HashMap<>();
        if (!serviceClass.ValidateProductId(id)) {
            response.put("message", "Product not present in database");
            return response;
        }
        inventoryRepository.deleteByProductId(id);
        productRepository.deleteById(id);
        response.put("message", "Product deleted successfully");
        return response;
    }

    @GetMapping("validate/{quantity}/{storeId}/{productId}")
    public boolean validateQuantity(@PathVariable("quantity") Integer quantity,
            @PathVariable("storeId") Long storeId, @PathVariable("productId") Long productId) {
        Inventory inventory = inventoryRepository.findByProductIdandStoreId(productId, storeId);
        return inventory != null && inventory.getStockLevel() != null && inventory.getStockLevel() >= quantity;
    }
}
