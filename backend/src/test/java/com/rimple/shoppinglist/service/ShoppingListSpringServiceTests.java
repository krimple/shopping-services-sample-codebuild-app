package com.rimple.shoppinglist.service;


import com.rimple.shoppinglist.model.ShoppingListItem;
import org.junit.After;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ShoppingListSpringServiceTests {

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    @Qualifier("dynamoDBService")
    private ShoppingListService shoppingListService;

    @Autowired
    private ApplicationContext context;

    @AfterAll
    void resetList() {
        shoppingListService.removeAllItems("KensList");

    }
    @Test
    void doAllRight() {
        assertEquals(2, 2);
    }

    @Test
    void springBooted() throws Exception {
        assertNotNull(context);
    }

    @Test
    void testCreateAndFetchItem() {
        String itemKey = shoppingListService.addItem(
               "KensList",
               new ShoppingListItem("Eggs", new BigDecimal("3.50")));
        assertNotNull(shoppingListService.getItem("KensList", itemKey));
    }

    @Test
    void testCreateAndUpdateAndFetchItem() {
        String itemKey = shoppingListService.addItem(
                "KensList",
                new ShoppingListItem("Eggs", new BigDecimal("3.50")));

        shoppingListService.updateItem("KensList",
               new ShoppingListItem(itemKey, "Eggs", new BigDecimal("2.25")));

        ShoppingListItem item = shoppingListService.getItem("KensList", itemKey);
        assertEquals(new BigDecimal("2.25"), item.getPrice());
    }

    @Test
    void testDeleteItem() {
        String key = shoppingListService.addItem(
                "KensList",
                new ShoppingListItem("Eggs", new BigDecimal("3.50")));

        shoppingListService.deleteItem("KensList", key);

        assertNull(shoppingListService.getItem("KensList", key));
    }

}
