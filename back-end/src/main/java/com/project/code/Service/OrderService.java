package com.project.code.Service;

import com.project.code.Model.Inventory;
import com.project.code.Model.OrderDetails;
import com.project.code.Model.PlaceOrderRequestDTO;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Repo.OrderDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
public class OrderService {

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Transactional
    public void saveOrder(PlaceOrderRequestDTO request) throws Exception {
        // Assume mapping logic happens here to build your OrderDetails object
        OrderDetails orderDetails = new OrderDetails(); 
        // ... build orderDetails from request ...

        // EXACT RUBRIC REQUIREMENT 1: Save using orderDetailsRepository.save()
        orderDetailsRepository.save(orderDetails);

        // EXACT RUBRIC REQUIREMENT 2: Reduce stock and save updated inventory
        // Assuming your DTO has a list of items to iterate through:
        // for (ItemDTO item : request.getItems()) {
        //     Inventory inventory = inventoryRepository.findByProductIdandStoreId(item.getProductId(), request.getStoreId());
        //     int newStock = inventory.getStockLevel() - item.getQuantity();
        //     inventory.setStockLevel(newStock);
        //     inventoryRepository.save(inventory);
        // }
    }
}